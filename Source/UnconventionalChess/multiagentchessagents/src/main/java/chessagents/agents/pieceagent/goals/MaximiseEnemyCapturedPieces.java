package chessagents.agents.pieceagent.goals;

import chessagents.chess.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class MaximiseEnemyCapturedPieces extends Value {

    public MaximiseEnemyCapturedPieces() {
        super("Maximise captured pieces");
    }

    @Override
    public boolean actionMaintainsValue(ChessPiece pieceWithValue, GameState gameState, PieceAction pieceAction) {
        var enemiesCapturedBefore = gameState.getCapturedForColour(pieceWithValue.getColour()).size();
        var enemiesCapturedAfter = gameState.getOutcomeOfAction(pieceAction).getCapturedForColour(pieceWithValue.getColour()).size();
        return enemiesCapturedBefore < enemiesCapturedAfter;
    }
}