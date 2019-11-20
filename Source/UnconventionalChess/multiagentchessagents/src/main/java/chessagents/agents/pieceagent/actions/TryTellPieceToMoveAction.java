package chessagents.agents.pieceagent.actions;

import chessagents.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class TryTellPieceToMoveAction extends PieceAction {
    public TryTellPieceToMoveAction(ChessPiece me) {
        super(PieceTransition.TOLD_PIECE_TO_MOVE, "Try tell piece to move", me);
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        // TODO
        return gameState;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        // TODO apply move to state
        return gameState;
    }
}
