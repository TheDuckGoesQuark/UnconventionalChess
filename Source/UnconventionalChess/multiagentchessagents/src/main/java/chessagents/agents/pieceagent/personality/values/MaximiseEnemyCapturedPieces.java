package chessagents.agents.pieceagent.personality.values;

import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.argumentation.Opinion;
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

        if (enemiesCapturedBefore < enemiesCapturedAfter) {
            return MoveResponse.buildResponse(action, Opinion.LIKE, this);
        } else if (enemiesCapturedBefore == enemiesCapturedAfter) {
            return MoveResponse.buildResponse(action, Opinion.NEUTRAL, this);
        } else {
            return MoveResponse.buildResponse(action, Opinion.DISLIKE, this);
        }
    }
}
