package chessagents.agents.pieceagent.actions;

import chessagents.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;

public class PerformMoveAction extends PieceAction {

    private final PieceMove move;

    /**
     * @param actor piece performing the action
     */
    public PerformMoveAction(ChessPiece actor, PieceMove move) {
        super(PieceTransition.MOVE_PERFORMED, "Perform move", actor);
        this.move = move;
    }


    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        return getOutcomeOfAction(gameState);
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState.makeMove(move);
    }
}
