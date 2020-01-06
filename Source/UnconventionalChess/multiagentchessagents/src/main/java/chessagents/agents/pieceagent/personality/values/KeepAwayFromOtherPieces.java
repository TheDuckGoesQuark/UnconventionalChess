package chessagents.agents.pieceagent.personality.values;

import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.argumentation.Opinion;
import chessagents.agents.pieceagent.argumentation.Reasoning;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.ontology.schemas.concepts.Position;

import static chessagents.chess.ChessUtil.areAdjacentCoordinates;

public class KeepAwayFromOtherPieces extends Value {
    public KeepAwayFromOtherPieces() {
        super("Keep away from other pieces");
    }

    @Override
    public MoveResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        var piecesNearBefore = piecesNearMe(gameState, chessPiece);
        var piecesNearAfter = piecesNearMe(gameState.applyMove(action), chessPiece);

        if (piecesNearBefore && !piecesNearAfter) {
            return MoveResponse.buildResponse(action, Opinion.LIKE, new Reasoning(this, "keep me away from other pieces"));
        } else if ((!piecesNearBefore && !piecesNearAfter)) {
            return MoveResponse.buildResponse(action, Opinion.NEUTRAL, new Reasoning(this, "keep me away from other pieces"));
        } else {
            return MoveResponse.buildResponse(action, Opinion.DISLIKE, new Reasoning(this, "not keep me away from other pieces"));
        }
    }

    private boolean piecesNearMe(GameState gameState, ChessPiece me) {
        var myPos = me.getPosition().getCoordinates();

        return gameState.getAllPiecesOnBoard().stream()
                .map(ChessPiece::getPosition)
                .map(Position::getCoordinates)
                .filter(p -> !p.equals(myPos))
                .anyMatch(otherPosition -> areAdjacentCoordinates(myPos, otherPosition));
    }
}
