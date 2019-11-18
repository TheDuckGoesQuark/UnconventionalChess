package chessagents.agents.pieceagent.planner.goal;

import chessagents.GameState;
import chessagents.agents.pieceagent.planner.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class MaximiseCapturedPieces extends Value {

    public MaximiseCapturedPieces() {
        super("Maximise captured pieces");
    }

    @Override
    public boolean actionMaintainsValue(ChessPiece pieceWithValue, GameState gameState, PieceAction pieceAction) {
        var enemiesCapturedBefore = gameState.getCapturedForColour(pieceWithValue.getColour()).size();
        var enemiesCapturedAfter = gameState.apply(pieceAction).getCapturedForColour(pieceWithValue.getColour()).size();
        return enemiesCapturedBefore < enemiesCapturedAfter;
    }
}
