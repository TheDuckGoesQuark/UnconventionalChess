package chessagents.agents.pieceagent.actions;


import chessagents.chess.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.lang.acl.ACLMessage;

public class AgreeToMoveAction extends PieceAction {

    private final ACLMessage requestToMove;
    private final PieceMove requestedMove;

    /**
     * @param actor         piece performing the action
     * @param requestToMove request to move message
     * @param requestedMove move that has been requested be made
     */
    public AgreeToMoveAction(ChessPiece actor, ACLMessage requestToMove, PieceMove requestedMove) {
        super(PieceTransition.AGREED_TO_MAKE_MOVE, "Agree to move", actor, true);
        this.requestToMove = requestToMove;
        this.requestedMove = requestedMove;
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        sendAgree(actor, requestToMove);
        return gameState;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState.makeMove(requestedMove);
    }

    private void sendAgree(PieceAgent actor, ACLMessage message) {
        var agree = message.createReply();
        agree.setPerformative(ACLMessage.AGREE);
        agree.removeReceiver(actor.getAID());
        actor.send(agree);
    }
}
