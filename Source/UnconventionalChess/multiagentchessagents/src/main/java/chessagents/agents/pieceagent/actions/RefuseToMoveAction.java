package chessagents.agents.pieceagent.actions;

import chessagents.chess.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class RefuseToMoveAction extends PieceAction {
    /**
     * @param actor piece performing the action
     */
    public RefuseToMoveAction(ChessPiece actor) {
        super(PieceTransition.NOT_MOVING, "Refuse to move", actor);
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        return gameState;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState;
    }
}
