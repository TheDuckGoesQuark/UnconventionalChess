package chessagents.agents.pieceagent.goals;

import chessagents.agents.pieceagent.ActionResponse;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;

public class MaximiseEnemyCapturedPieces extends Value {

    public MaximiseEnemyCapturedPieces() {
        super("Maximise captured pieces");
    }

    @Override
    public ActionResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        var enemiesCapturedBefore = gameState.getCapturedForColour(chessPiece.getColour()).size();
        var enemiesCapturedAfter = gameState.applyMove(action).getCapturedForColour(chessPiece.getColour()).size();
        var approved = enemiesCapturedBefore < enemiesCapturedAfter;

        return new ActionResponse(action, approved);
    }
}
