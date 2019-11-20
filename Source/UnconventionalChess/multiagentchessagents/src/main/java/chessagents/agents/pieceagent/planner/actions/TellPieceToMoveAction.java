package chessagents.agents.pieceagent.planner.actions;

import chessagents.GameState;
import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.planner.PieceAction;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.Optional;
import java.util.stream.Collectors;

public class TellPieceToMoveAction extends PieceAction {
    private final PieceMove move;
    private final ChessPiece otherChessPiece;

    public TellPieceToMoveAction(ChessPiece actor, PieceMove move, ChessPiece otherChessPiece) {
        super(PieceTransition.TOLD_PIECE_TO_MOVE, "Tell piece to move", actor);
        this.move = move;
        this.otherChessPiece = otherChessPiece;
    }

    @Override
    public ChessPiece getActor() {
        return null;
    }

    @Override
    public Optional<PieceMove> getMove() {
        return Optional.of(move);
    }

    @Override
    public void perform(PieceAgent actor, GameState gameState) {
        var request = createRequestToMove(actor, gameState.getPieceAtPosition(move.getSource()).get().getAgentAID(), move);
        sendRequestMove(actor, gameState, request);
    }

    private void sendRequestMove(PieceAgent actor, GameState gameState, ACLMessage request) {
        var aids = gameState.getAllPieces().stream()
                .map(ChessPiece::getAgentAID)
                .collect(Collectors.toSet());

        // send to everyone so they know not to expect a CFP to speak
        aids.forEach(request::addReceiver);

        // tell actual recipient that they should also let everyone know of their response
        aids.forEach(request::addReplyTo);

        actor.send(request);
    }

    private ACLMessage createRequestToMove(PieceAgent actor, AID movingPiece, PieceMove move) {
        var request = ChessMessageBuilder.constructMessage(ACLMessage.REQUEST);
        var makeMove = new MakeMove(move);
        var action = new Action(movingPiece, makeMove);

        try {
            actor.getContentManager().fillContent(request, action);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to create make move request: " + e.getMessage());
        }

        return request;
    }
}
