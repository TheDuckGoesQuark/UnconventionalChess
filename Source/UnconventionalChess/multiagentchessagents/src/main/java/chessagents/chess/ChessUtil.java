package chessagents.chess;

public class ChessUtil {
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

    public static boolean areAdjacentCoordinates(String a, String b) {
        return new Coordinate(a).isAdjacent(new Coordinate(b));
    }
}
