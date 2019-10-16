package chessagents.chess;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;

/**
 * Middleware for translating from ontological chess classes and chess library classes using string representations
 */
public class BoardWrapper {

    private final Board board;

    public BoardWrapper() {
        board = new Board();
    }

    private static Move constructMove(String source, String target) {
        final Square from = Square.fromValue(source);
        final Square to = Square.fromValue(target);
        return new Move(from, to);
    }

    public void makeMove(String source, String target) {
        board.doMove(constructMove(source, target));
    }

    public boolean isValidMove(String source, String target) {
        try {
            final Move move = constructMove(source, target);
            return MoveGenerator.generateLegalMoves(board).contains(move);
        } catch (MoveGeneratorException e) {
            return false;
        }
    }

    public boolean isSideToGo(String humanSide) {
        return board.getSideToMove().value().equals(humanSide);
    }
}
