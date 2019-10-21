package chessagents.agents.gameagent.behaviours;

import chessagents.agents.gameagent.GameAgentProperties;
import chessagents.agents.pieceagent.*;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Colour;
import com.github.bhlangonijr.chesslib.PieceType;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.ContainerID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.util.Logger;
import jade.wrapper.ControllerException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SetupGame extends OneShotBehaviour {

    private static final Logger logger = Logger.getMyLogger(SetupGame.class.getName());
    private final GameAgentProperties properties;
    private final Set<AID> pieceAgents;
    private final BoardWrapper board;

    public SetupGame(GameAgentProperties properties, Set<AID> pieceAgents, BoardWrapper board) {
        this.properties = properties;
        this.pieceAgents = pieceAgents;
        this.board = board;
    }

    private static String generatePieceAgentName(String pieceType, String colour, String startingSquare) {
        return startingSquare + "-" + colour + "-" + pieceType;
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

        // start listening agents asking that the game is ready
        myAgent.addBehaviour(new HandleGameMetaQueries(myAgent, pieceAgents));

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

        agentColours.forEach(this::spawnPieceAgentsForSide);
    }

    private void spawnPieceAgentsForSide(String colour) {
        logger.info("Spawning agents for side " + colour);
        board.getPositionsOfAllPiecesForColour(colour)
                .forEach(sq -> spawnPieceAgent(sq, colour, board.getPieceTypeAtSquare(sq)));
    }

    private void spawnPieceAgent(String startingSquare, String colour, String type) {
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
            myAgent.addBehaviour(new AchieveREInitiator(myAgent, request) {
                protected void handleInform(ACLMessage inform) {
                    logger.info("Agent " + agentName + "successfully created");
                    pieceAgents.add(new AID(agentName, false));
                }

                protected void handleFailure(ACLMessage failure) {
                    logger.warning("Error creating agent " + agentName + ":" + failure.getContent());
                    // TODO inform user of failure, cancel game creation and cleanup
                }
            });
        } catch (Exception e) {
            logger.warning("Error creating agent " + agentName + ":" + e.getMessage());
        }
    }

}
