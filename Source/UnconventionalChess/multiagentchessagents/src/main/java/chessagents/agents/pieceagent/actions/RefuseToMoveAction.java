package chessagents.agents.pieceagent.actions;

import chessagents.chess.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.lang.acl.ACLMessage;

public class RefuseToMoveAction extends PieceAction {
    private final ACLMessage requestToMove;
    private final PieceMove requestedMove;

    /**
     * @param actor         piece performing the action
     * @param requestToMove the message that was sent to this agent requesting them to move
     * @param requestedMove the move that was requested of this agent to make
     */
    public RefuseToMoveAction(ChessPiece actor, ACLMessage requestToMove, PieceMove requestedMove) {
        super(PieceTransition.NOT_MOVING, "Refuse to move", actor, true);
        this.requestToMove = requestToMove;
        this.requestedMove = requestedMove;
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        sendRefuse(actor, requestToMove);
        return gameState;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState;
    }

    private void sendRefuse(PieceAgent actor, ACLMessage message) {
        var refuse = message.createReply();
        refuse.setPerformative(ACLMessage.REFUSE);
        refuse.removeReceiver(actor.getAID());
        actor.send(refuse);
    }
}
