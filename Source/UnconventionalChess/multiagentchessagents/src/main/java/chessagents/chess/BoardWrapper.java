package chessagents.chess;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Middleware for translating from ontological chess classes and chess library classes using string representations
 */
public class BoardWrapper {

    private final Random random = new Random();
    private final Board board;

    public BoardWrapper() {
        board = new Board();
    }

    /**
     * Constructs an instance of library move object from string coordinates
     *
     * @param source source square
     * @param target target square
     * @return move instance
     */
    private static Move constructMove(String source, String target) {
        final Square from = Square.fromValue(source);
        final Square to = Square.fromValue(target);
        return new Move(from, to);
    }

    /**
     * Performs move from given source to given target
     *
     * @param source source coordinate
     * @param target target coordinate
     */
    public void makeMove(String source, String target) {
        board.doMove(constructMove(source, target));
    }

    /**
     * @param source source of move
     * @param target target of move
     * @return true if move can currently be performed
     */
    public boolean isValidMove(String source, String target) {
        try {
            final Move move = constructMove(source, target);
            return MoveGenerator.generateLegalMoves(board).contains(move);
        } catch (MoveGeneratorException e) {
            return false;
        }
    }

    /**
     * @param colour colour to query
     * @return true if the given colour is next to go
     */
    public boolean isSideToGo(String colour) {
        return board.getSideToMove().value().equals(colour);
    }

    /**
     * @param sq the square to query for
     * @return the name of the piece at the given square
     */
    public String getPieceTypeAtSquare(String sq) {
        return board.getPiece(Square.valueOf(sq)).getPieceType().value();
    }

    /**
     * @param colour colour of pieces to query for
     * @return all the current positions for pieces of the given colour
     */
    public Set<String> getPositionsOfAllPiecesForColour(String colour) {
        final Side side = Side.valueOf(colour);

        return Arrays.stream(Square.values())
                .filter(sq -> board.getPiece(sq) != Piece.NONE)
                .filter(sq -> board.getPiece(sq).getPieceSide().equals(side))
                .map(Square::value)
                .collect(Collectors.toSet());
    }

    public Optional<chessagents.ontology.schemas.concepts.Move> getRandomMove() {
        try {
            MoveList moves = MoveGenerator.generateLegalMoves(board);
            Move move = moves.get(random.nextInt(moves.size()));

            return Optional.of(
                    new chessagents.ontology.schemas.concepts.Move(move.getFrom().value(), move.getTo().value())
            );
        } catch (MoveGeneratorException e) {
            return Optional.empty();
        }
    }

}
