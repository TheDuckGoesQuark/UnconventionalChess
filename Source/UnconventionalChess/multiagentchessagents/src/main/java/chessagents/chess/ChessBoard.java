package chessagents.chess;

import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.ontology.schemas.concepts.Position;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import jade.content.OntoAID;
import jade.core.AID;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Middleware for translating from ontological chess classes and chess library classes
 */
public class ChessBoard {

    /**
     * Random generator for general use
     */
    private static final Random random = new Random();
    /**
     * The set of pieces on the board
     */
    private final Set<ChessPiece> chessPieces;
    /**
     * The actual board with game logic implemented
     */
    private final Board board;

    /**
     * Constructs chessboard with pieces initialised with AIDs unknown
     */
    public ChessBoard() {
        board = new Board();
        chessPieces = Arrays.stream(Square.values())
                .filter(sq -> board.getPiece(sq) != Piece.NONE)
                .map(sq -> {
                    var piece = board.getPiece(sq);
                    var colour = new Colour(piece.getPieceSide().name());
                    var type = piece.getPieceType().name();
                    var position = new Position(sq.value());
                    return new ChessPiece(colour, type, position);
                })
                .collect(Collectors.toSet());
    }

    /**
     * Private constructor for constructing a chessboard with the given set of pieces and board
     *
     * @param board  current board
     * @param pieces set of pieces
     */
    private ChessBoard(Board board, Set<ChessPiece> pieces) {
        this.board = board;
        this.chessPieces = pieces;
    }

    /**
     * Constructs an instance of library move object from string coordinates
     *
     * @param move move to construct from
     * @return move instance
     */
    private static Move constructChessLibMove(PieceMove move) {
        final Square from = Square.fromValue(move.getSource().getCoordinates());
        final Square to = Square.fromValue(move.getTarget().getCoordinates());
        return new Move(from, to);
    }

    /**
     * Constructs move concept from chess lib move
     *
     * @param move chess lib move instance
     * @return concept move
     */
    private static PieceMove constructPieceMove(Move move) {
        return new PieceMove(
                move.getFrom().value(),
                move.getTo().value()
        );
    }

    public Optional<PieceMove> getRandomMove() {
        try {
            MoveList moves = MoveGenerator.generateLegalMoves(board);
            Move move = moves.get(random.nextInt(moves.size()));

            return Optional.of(constructPieceMove(move));
        } catch (MoveGeneratorException e) {
            return Optional.empty();
        }
    }

    public boolean gameIsOver() {
        return board.isMated() || board.isDraw() || board.isStaleMate() || board.isInsufficientMaterial();
    }

    /**
     * Performs move from given source to given target
     *
     * @param move move to make
     */
    public void makeMove(PieceMove move) {
        board.doMove(constructChessLibMove(move));

        // update position of piece concept
        chessPieces.stream()
                .map(ChessPiece::getPosition)
                .filter(p -> p.equals(move.getSource()))
                .forEach(p -> p.setCoordinates(move.getTarget().getCoordinates()));
    }

    /**
     * @param pieceMove move to verify
     * @return true if move can currently be performed
     */
    public boolean isValidMove(PieceMove pieceMove) {
        try {
            final Move move = constructChessLibMove(pieceMove);
            return MoveGenerator.generateLegalMoves(board).contains(move);
        } catch (MoveGeneratorException e) {
            return false;
        }
    }

    /**
     * @param colour colour to query
     * @return true if the given colour is next to go
     */
    public boolean isSideToGo(Colour colour) {
        return board.getSideToMove().equals(Side.fromValue(colour.getColour()));
    }

    /**
     * Creates a copy of the current board, and performs the given move on the copy, then returns the copy
     *
     * @param move move to be performed
     * @return copy of this board with move performed
     */
    public ChessBoard copyOnMove(PieceMove move) {
        var thisCopy = new ChessBoard(board.clone(), chessPieces);
        thisCopy.makeMove(move);
        return thisCopy;
    }

    /**
     * Gets the set of pieces that are currently threatened
     *
     * @return the set of pieces that are currently threatened
     */
    public Set<ChessPiece> getThreatenedPieces(Set<ChessPiece> chessPieces) {
        var threatened = new HashSet<ChessPiece>();
        var currentSideToGo = board.getSideToMove();

        var currentPositions = chessPieces.stream()
                .map(ChessPiece::getPosition)
                .map(Position::getCoordinates)
                .collect(Collectors.toSet());

        for (Side side : Side.values()) {
            board.setSideToMove(side);
            try {
                // from the set of all moves, get all that end in the place of any other piece and return the position
                var threatenedPositions = MoveGenerator.generateLegalMoves(board).stream()
                        .map(Move::getTo)
                        .map(Square::value)
                        .filter(currentPositions::contains)
                        .map(Position::new)
                        .collect(Collectors.toSet());

                // get all the pieces that fall in the positions in the set of threatened positions
                chessPieces.stream()
                        .filter(p -> threatenedPositions.contains(p.getPosition()))
                        .forEach(threatened::add);
            } catch (MoveGeneratorException e) {
                // do nothing
            }
        }

        // restore board
        board.setSideToMove(currentSideToGo);

        return threatened;
    }

    /**
     * Registers a piece with an agent AID
     * @param piece piece to have AID attached to it
     * @param pieceAgentAID AID of agent representing that piece
     */
    public void registerAgentPiece(ChessPiece piece, OntoAID pieceAgentAID) {
        chessPieces.stream().filter(p -> p.equals(piece)).findFirst().ifPresent(p -> p.setAgentAID(pieceAgentAID));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return board.equals(that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board);
    }
}
