package chessagents.agents.gameagent.behaviours;

import chessagents.agents.gameagent.GameAgentProperties;
import chessagents.agents.pieceagent.*;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class SetupGame extends OneShotBehaviour {

    private static final Logger logger = Logger.getMyLogger(SetupGame.class.getName());
    private GameAgentProperties properties;
    private Map<Side, Set<AID>> agentsBySide;
    private Board board;

    public SetupGame(GameAgentProperties properties, Map<Side, Set<AID>> agentsBySide, Board board) {
        this.properties = properties;
        this.agentsBySide = agentsBySide;
        this.board = board;
    }

    @Override
    public void action() {
        if (!properties.isHumanPlays()) {
            spawnPieceAgentsForSide(Side.WHITE);
            spawnPieceAgentsForSide(Side.BLACK);
        } else {
            if (properties.isHumanPlaysAsWhite()) {
                spawnPieceAgentsForSide(Side.BLACK);
            } else {
                spawnPieceAgentsForSide(Side.WHITE);
            }
        }
    }

    private void spawnPieceAgentsForSide(Side colour) {
        agentsBySide.put(colour, new HashSet<>());
        logger.info("Spawning agents for side " + colour.value());
        Stream.of(Square.values())
                .filter(sq -> !board.getPiece(sq).equals(Piece.NONE))
                .filter(sq -> board.getPiece(sq).getPieceSide().equals(colour))
                .forEach(sq -> spawnPieceAgent(board.getPiece(sq), sq));
    }

    private String generatePieceAgentName(Piece piece, Square startingSquare) {
        return startingSquare.value() + "-" + piece.getPieceSide().name() + "-" + piece.getPieceType().name();
    }

    private Optional<String> getPieceClassname(Piece piece) {
        switch (piece.getPieceType()) {
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

    private void spawnPieceAgent(Piece piece, Square startingSquare) {
        final String agentName = generatePieceAgentName(piece, startingSquare);
        final String agentClassName = getPieceClassname(piece)
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
        createAgent.addArguments(startingSquare.name());
        createAgent.addArguments(piece.getPieceSide().name());

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
                    agentsBySide.get(piece.getPieceSide()).add(new AID(agentName, false));
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
