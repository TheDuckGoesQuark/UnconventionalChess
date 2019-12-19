package chessagents.agents.pieceagent.personality.values;

import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.argumentation.Opinion;
import chessagents.agents.pieceagent.argumentation.Reasoning;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.ontology.schemas.concepts.Position;

public class Shy extends Value {
    public Shy() {
        super("Shy");
    }

    @Override
    public MoveResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        var piecesNearBefore = piecesNearMe(gameState, chessPiece);
        var piecesNearAfter = piecesNearMe(gameState.applyMove(action), chessPiece);

        if (piecesNearBefore && !piecesNearAfter) {
            return MoveResponse.buildResponse(action, Opinion.LIKE, new Reasoning(this, "keeps me away from other pieces"));
        } else if ((!piecesNearBefore && !piecesNearAfter)) {
            return MoveResponse.buildResponse(action, Opinion.NEUTRAL, new Reasoning(this, "keeps me away from other pieces"));
        } else {
            return MoveResponse.buildResponse(action, Opinion.DISLIKE, new Reasoning(this, "I don't want to be near other pieces"));
        }
    }

    private boolean piecesNearMe(GameState gameState, ChessPiece me) {
        var myPos = me.getPosition().getCoordinates();

        return gameState.getAllPieces().stream()
                .map(ChessPiece::getPosition)
                .map(Position::getCoordinates)
                .filter(p -> !p.equals(myPos))
                .anyMatch(otherPosition -> isAdjacent(myPos, otherPosition));
    }

    static class Coordinate {
        private char col;
        private int row;

        public Coordinate(String coord) {
            var tokens = coord.toCharArray();
            col = tokens[0];
            row = Character.getNumericValue(tokens[1]);
        }

        public boolean isAdjacent(Coordinate b) {
            return Math.abs(col - b.col) == 1 && Math.abs(row - b.row) == 1;
        }
    }

    private static boolean isAdjacent(String a, String b) {
        return new Coordinate(a).isAdjacent(new Coordinate(b));
    }
}
