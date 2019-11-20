package chessagents.agents.pieceagent.actions;

import chessagents.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class PerformMoveAction extends PieceAction {
    /**
     * @param actor piece performing the action
     */
    public PerformMoveAction(ChessPiece actor) {
        super(PieceTransition.MOVE_PERFORMED, "Perform move", actor);
    }


    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        return performOnStateOnly(gameState);
    }

    @Override
    public GameState performOnStateOnly(GameState gameState) {
        return null;
    }
}
