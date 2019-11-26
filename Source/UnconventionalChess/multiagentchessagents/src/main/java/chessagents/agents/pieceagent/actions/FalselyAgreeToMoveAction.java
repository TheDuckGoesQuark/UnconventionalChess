package chessagents.agents.pieceagent.actions;

import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.lang.acl.ACLMessage;

public class FalselyAgreeToMoveAction extends AgreeToMoveAction {
    /**
     * @param actor         piece performing the action
     * @param requestToMove request to move message
     * @param requestedMove move that has been requested be made
     */
    public FalselyAgreeToMoveAction(ChessPiece actor, ACLMessage requestToMove, PieceMove requestedMove) {
        super(actor, requestToMove, requestedMove);
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        // since we're lying about moving, the outcome of this action is actually nothing!
        return gameState;
    }
}
