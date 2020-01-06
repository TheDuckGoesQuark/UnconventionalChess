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
            var sameCol = col == b.col;
            var sameRow = row == b.row;
            var colsAdjacent = Math.abs(col - b.col) == 1;
            var rowsAdjacent = Math.abs(row - b.row) == 1;
            return (sameCol && rowsAdjacent)
                    || (sameRow && colsAdjacent)
                    || (colsAdjacent && rowsAdjacent);
        }
    }

    public static boolean areAdjacentCoordinates(String a, String b) {
        return new Coordinate(a).isAdjacent(new Coordinate(b));
    }
}
