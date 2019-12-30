package chessagents.agents.pieceagent.personality.values;

import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.argumentation.Opinion;
import chessagents.agents.pieceagent.argumentation.Reasoning;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.ontology.schemas.concepts.Position;

public class KeepNearOtherPieces extends Value {
    public KeepNearOtherPieces() {
        super("Keep near other pieces");
    }

    @Override
    public MoveResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        var piecesNearBefore = countPiecesNearMe(gameState, chessPiece);
        var piecesNearAfter = countPiecesNearMe(gameState.applyMove(action), chessPiece);

        if (piecesNearBefore < piecesNearAfter) {
            return MoveResponse.buildResponse(action, Opinion.LIKE, new Reasoning(this, "keep me close to my friends"));
        } else if (piecesNearBefore == piecesNearAfter) {
            return MoveResponse.buildResponse(action, Opinion.NEUTRAL, new Reasoning(this, "not help me find friends"));
        } else {
            return MoveResponse.buildResponse(action, Opinion.DISLIKE, new Reasoning(this, "put me further away from my friends"));
        }
    }

    private int countPiecesNearMe(GameState gameState, ChessPiece me) {
        var myPos = me.getPosition().getCoordinates();

        return (int) gameState.getAllPieces().stream()
                .map(ChessPiece::getPosition)
                .map(Position::getCoordinates)
                .filter(p -> !p.equals(myPos))
                .filter(otherPosition -> isAdjacent(myPos, otherPosition))
                .count();
    }

    private static boolean isAdjacent(String a, String b) {
        return new KeepAwayFromOtherPieces.Coordinate(a).isAdjacent(new KeepAwayFromOtherPieces.Coordinate(b));
    }
}
