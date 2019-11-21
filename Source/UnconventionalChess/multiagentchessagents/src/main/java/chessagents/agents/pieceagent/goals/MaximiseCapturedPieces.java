package chessagents.agents.pieceagent.goals;

import chessagents.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class MaximiseCapturedPieces extends Value {

    public MaximiseCapturedPieces() {
        super("Maximise captured pieces");
    }

    @Override
    public boolean actionMaintainsValue(ChessPiece pieceWithValue, GameState gameState, PieceAction pieceAction) {
        var enemiesCapturedBefore = gameState.getCapturedForColour(pieceWithValue.getColour()).size();
        var enemiesCapturedAfter = gameState.getOutcomeOfAction(pieceAction).getCapturedForColour(pieceWithValue.getColour()).size();
        return enemiesCapturedBefore < enemiesCapturedAfter;
    }
}
