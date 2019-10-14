package chessagents.agents.gameagent.behaviours;

import chessagents.agents.pieceagent.*;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.ContainerID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.wrapper.ControllerException;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Stream;

// TODO this
public class SetupGame extends OneShotBehaviour {
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
            containerName = getContainerController().getContainerName();
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

        final Action requestAction = new Action(getAMS(), createAgent);
        final ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(getAMS());
        request.setOntology(JADEManagementOntology.getInstance().getName());

        getContentManager().registerLanguage(new SLCodec(),
                FIPANames.ContentLanguage.FIPA_SL);

        getContentManager().registerOntology(JADEManagementOntology.getInstance());

        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        try {
            getContentManager().fillContent(request, requestAction);
            addBehaviour(new AchieveREInitiator(this, request) {
                protected void handleInform(ACLMessage inform) {
                    logger.info("Agent " + agentName + "successfully created");
                    agentsBySide.get(piece.getPieceSide()).add(agentName);
                }

                protected void handleFailure(ACLMessage failure) {
                    logger.warning("Error creating agent " + agentName + ".");
                    // TODO inform user of failure, cancel game creation and cleanup
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
