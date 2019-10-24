package chessagents.agents.gameagent.behaviours;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameAgentProperties;
import chessagents.agents.pieceagent.*;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Game;
import chessagents.ontology.schemas.concepts.Piece;
import chessagents.ontology.schemas.concepts.Position;
import com.github.bhlangonijr.chesslib.PieceType;
import jade.content.OntoAID;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.util.Logger;
import jade.wrapper.ControllerException;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static chessagents.agents.gameagent.GameAgent.GAME_STATUS_KEY;
import static chessagents.ontology.ChessOntology.IS_READY;

public class SpawnPieceAgents extends OneShotBehaviour {

    private static final Logger logger = Logger.getMyLogger(SpawnPieceAgents.class.getName());
    private final GameAgentProperties properties;
    private final Game game;
    private final DataStore dataStore;

    public SpawnPieceAgents(GameAgentProperties properties, Game game, DataStore dataStore) {
        this.properties = properties;
        this.dataStore = dataStore;
        this.game = game;
    }

    private String generatePieceAgentName(String pieceType, String colour, String startingSquare) {
        return game.getGameId() + "-" + startingSquare + "-" + colour + "-" + pieceType;
    }

    private static Optional<String> mapPieceTypeToAgentClass(String type) {
        final PieceType pieceType = PieceType.valueOf(type);
        switch (pieceType) {
            case PAWN:
                return Optional.ofNullable(PawnAgent.class.getName());
            case KNIGHT:
                return Optional.ofNullable(KnightAgent.class.getName());
            case BISHOP:
                return Optional.ofNullable(BishopAgent.class.getName());
            case ROOK:
                return Optional.ofNullable(RookAgent.class.getName());
            case QUEEN:
                return Optional.ofNullable(QueenAgent.class.getName());
            case KING:
                return Optional.ofNullable(KingAgent.class.getName());
            case NONE:
            default:
                return Optional.empty();
        }
    }

    @Override
    public void action() {
        final Set<String> agentColours = new HashSet<>(2);

        // Determine colours to generate agents for
        if (!properties.isHumanPlays()) {
            agentColours.add(Colour.WHITE);
            agentColours.add(Colour.BLACK);
        } else {
            if (properties.isHumanPlaysAsWhite()) {
                agentColours.add(Colour.BLACK);
            } else {
                agentColours.add(Colour.WHITE);
            }
        }

        // build up sequence of spawn agent behaviours followed by an update to the game status
        var sequence = new SequentialBehaviour();
        agentColours.stream()
                .map(this::getBehavioursForSpawningAllPiecesOnSide)
                .forEach(behaviours -> behaviours.forEach(sequence::addSubBehaviour));
        sequence.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                dataStore.put(GAME_STATUS_KEY, IS_READY);
            }
        });

        myAgent.addBehaviour(sequence);
    }

    private Set<Behaviour> getBehavioursForSpawningAllPiecesOnSide(String colour) {
        logger.info("Spawning agents for side " + colour);
        var board = (BoardWrapper) dataStore.get(GameAgent.BOARD_KEY);
        return board.getPositionsOfAllPiecesForColour(colour)
                .stream()
                .map(sq -> createSpawnPieceBehaviour(sq, colour, board.getPieceTypeAtSquare(sq)))
                .collect(Collectors.toSet());
    }

    private Behaviour createSpawnPieceBehaviour(String startingSquare, String colour, String type) {
        final String agentName = generatePieceAgentName(type, colour, startingSquare);
        final String agentClassName = mapPieceTypeToAgentClass(type)
                .orElseThrow(IllegalArgumentException::new);

        String containerName;
        try {
            containerName = myAgent.getContainerController().getContainerName();
        } catch (ControllerException e) {
            logger.warning("Unable to get current container name, using default. Reason: " + e.getMessage());
            containerName = "chess-container";
        }

        final CreateAgent createAgent = new CreateAgent();
        createAgent.setAgentName(agentName);
        createAgent.setClassName(agentClassName);
        createAgent.setContainer(new ContainerID(containerName, null));
        createAgent.addArguments(startingSquare);
        createAgent.addArguments(colour);
        createAgent.addArguments(myAgent.getAID().getName());
        createAgent.addArguments(Integer.toString(game.getGameId()));

        final Action requestAction = new Action(myAgent.getAMS(), createAgent);
        final ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(myAgent.getAMS());
        request.setOntology(JADEManagementOntology.getInstance().getName());

        myAgent.getContentManager().registerLanguage(new SLCodec(),
                FIPANames.ContentLanguage.FIPA_SL);

        myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());

        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        try {
            myAgent.getContentManager().fillContent(request, requestAction);
            return new AchieveREInitiator(myAgent, request) {
                protected void handleInform(ACLMessage inform) {
                    logger.info("Agent " + agentName + " successfully created");
                    var pieces = (Map<AID, Piece>) dataStore.get(GameAgent.AID_TO_PIECE_KEY);
                    var aid = new AID(agentName, AID.ISLOCALNAME);
                    var ontoAid = new OntoAID(aid.getLocalName(), AID.ISLOCALNAME);
                    var colourConcept = new Colour(colour);
                    var position = new Position(startingSquare);
                    pieces.put(aid, new Piece(ontoAid, colourConcept, type, position));
                }

                protected void handleFailure(ACLMessage failure) {
                    logger.warning("Error creating agent " + agentName + ":" + failure.getContent());
                    // TODO inform user of failure, cancel game creation and cleanup
                }
            };
        } catch (Exception e) {
            logger.warning("Error creating agent " + agentName + ":" + e.getMessage());
        }

        return null;
    }

}
