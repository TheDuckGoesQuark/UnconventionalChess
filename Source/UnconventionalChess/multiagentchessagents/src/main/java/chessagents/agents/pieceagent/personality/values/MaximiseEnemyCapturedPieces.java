package chessagents.agents.pieceagent.personality.values;

import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;

public class MaximiseEnemyCapturedPieces extends Value {

    public MaximiseEnemyCapturedPieces() {
        super("Maximise captured pieces");
    }

    @Override
    public MoveResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        var enemiesCapturedBefore = gameState.getCapturedForColour(chessPiece.getColour()).size();
        var enemiesCapturedAfter = gameState.applyMove(action).getCapturedForColour(chessPiece.getColour()).size();
        var approved = enemiesCapturedBefore < enemiesCapturedAfter;

        return new MoveResponse(action, approved);
    }
}
