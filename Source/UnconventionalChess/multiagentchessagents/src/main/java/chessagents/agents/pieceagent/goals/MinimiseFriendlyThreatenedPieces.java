package chessagents.agents.pieceagent.goals;

import chessagents.agents.pieceagent.ActionResponse;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;

public class MinimiseFriendlyThreatenedPieces extends Value {
    public MinimiseFriendlyThreatenedPieces() {
        super("Protect pieces");
    }

    @Override
    public ActionResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        var myColour = chessPiece.getColour();
        var threatenedPiecesBefore = gameState.getThreatenedPieces().stream().filter(p -> p.isColour(myColour)).count();
        var threatenedPiecesAfter = gameState.applyMove(action).getThreatenedPieces().stream().filter(p -> p.isColour(myColour)).count();
        var approves = threatenedPiecesBefore > threatenedPiecesAfter || (threatenedPiecesBefore == 0 && threatenedPiecesAfter == 0);

        return new ActionResponse(action, approves);
    }
}
