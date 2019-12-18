package chessagents.agents.pieceagent.personality.values;

import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.argumentation.Opinion;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;

public class MinimiseFriendlyThreatenedPieces extends Value {
    public MinimiseFriendlyThreatenedPieces() {
        super("Protect pieces");
    }

    @Override
    public MoveResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        var myColour = chessPiece.getColour();
        var threatenedPiecesBefore = gameState.getThreatenedPieces().stream().filter(p -> p.isColour(myColour)).count();
        var threatenedPiecesAfter = gameState.applyMove(action).getThreatenedPieces().stream().filter(p -> p.isColour(myColour)).count();

        var decreasesThreatenedFriendlies = threatenedPiecesBefore > threatenedPiecesAfter;
        var retainsNoThreatenedPieces = threatenedPiecesBefore == 0 && threatenedPiecesAfter == 0;

        if (decreasesThreatenedFriendlies || retainsNoThreatenedPieces) {
            return MoveResponse.buildResponse(action, Opinion.LIKE, this);
        } else if (threatenedPiecesBefore == threatenedPiecesAfter) {
            return MoveResponse.buildResponse(action, Opinion.NEUTRAL, this);
        } else {
            return MoveResponse.buildResponse(action, Opinion.DISLIKE, this);
        }
    }
}
