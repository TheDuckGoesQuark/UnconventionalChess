package chessagents.chess;

import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.ontology.schemas.concepts.Position;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import jade.content.OntoAID;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static chessagents.chess.ChessUtil.areAdjacentCoordinates;

/**
 * Wrapper for translating from ontological chess classes and chess library classes and managing game logic
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

    private final Set<ChessPiece> capturedPieces;

    /**
     * The actual board with game logic implemented
     */
    private final Board board;

    /**
     * Constructs chessboard with pieces initialised with AIDs unknown
     */
    public ChessBoard() {
        board = new Board();
        capturedPieces = new HashSet<>();
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
    private ChessBoard(Board board, Set<ChessPiece> pieces, Set<ChessPiece> capturedPieces) {
        this.board = board;
        this.chessPieces = pieces;
        this.capturedPieces = capturedPieces;
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

    /**
     * Performs move from given source to given target
     *
     * @param move move to make
     */
    public void makeMove(PieceMove move) {
        // clone captured piece and replace it in both sets as not on the board
        // We need to clone as to not affect other versions of the piece
        // i.e. if we are only testing this move, we don't want a piece to be taken off the board permanently
        getPieceAtPosition(move.getTarget()).ifPresent(p -> {
            var clone = p.clone();
            clone.removeFromBoard();
            capturedPieces.add(clone);
            chessPieces.remove(p); // remove current version
            chessPieces.add(clone); // replace with clone that has position off board
        });

        board.doMove(constructChessLibMove(move));

        // clone and update position of piece that moved
        getPieceAtPosition(move.getSource()).ifPresent(p -> {
            movePiece(p, move);
            // check for castling i.e. if we need to update multiple pieces
            if (castlingMove(p, move)) applyCastlingToRook(p, move);
        });
    }

    /**
     * Determines which rook is being castled with using the king and the direction of the kings movement, and
     * updates the position of the corresponding rook
     *
     * @param king     piece moving
     * @param kingMove move made
     */
    private void applyCastlingToRook(ChessPiece king, PieceMove kingMove) {
        final ChessPiece rook;
        final PieceMove rookMove;
        if (king.getColour().equals(Colour.WHITE)) {
            if (kingMove.getTarget().getCoordinates().equals("g1")) {
                var rookPos = new Position("h1");
                rook = getPieceAtPosition(rookPos).get();
                rookMove = new PieceMove(rookPos.getCoordinates(), "f1");
            } else {
                var rookPos = new Position("a1");
                rook = getPieceAtPosition(rookPos).get();
                rookMove = new PieceMove(rookPos.getCoordinates(), "d1");
            }
        } else {
            if (kingMove.getTarget().getCoordinates().equals("g8")) {
                var rookPos = new Position("h8");
                rook = getPieceAtPosition(rookPos).get();
                rookMove = new PieceMove(rookPos.getCoordinates(), "f8");
            } else {
                var rookPos = new Position("a8");
                rook = getPieceAtPosition(rookPos).get();
                rookMove = new PieceMove(rookPos.getCoordinates(), "d8");
            }
        }

        movePiece(rook, rookMove);
    }

    private boolean castlingMove(ChessPiece p, PieceMove move) {
        // if the king has move more than one square, then castling must have occurred
        return p.getType().equals(PieceType.KING.name())
                && !areAdjacentCoordinates(move.getSource().getCoordinates(), move.getTarget().getCoordinates());
    }

    private void movePiece(ChessPiece piece, PieceMove move) {
        var clone = piece.clone();
        clone.setPosition(move.getTarget());
        chessPieces.remove(piece); // remove current version
        chessPieces.add(clone); // replace with clone that has updated position
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
     * Gets the colour of the side to go
     *
     * @return colour of the side to go
     */
    public Colour getSideToGo() {
        return new Colour(board.getSideToMove().value());
    }

    /**
     * Creates a copy of the current board, and performs the given move on the copy, then returns the copy
     *
     * @param move move to be performed
     * @return copy of this board with move performed
     */
    public ChessBoard copyOnMove(PieceMove move) {
        // determine if we need to make a copy of the set of captured pieces
        final Set<ChessPiece> newCapturedPieces = getPieceAtPosition(move.getTarget()).isPresent() ? new HashSet<>(capturedPieces) : capturedPieces;
        // we always need to a new set of chess pieces since a piece moves each time and we need to clone that piece
        final Set<ChessPiece> newChessPieces = new HashSet<>(chessPieces);

        var thisCopy = new ChessBoard(board.clone(), newChessPieces, newCapturedPieces);
        thisCopy.makeMove(move);
        return thisCopy;
    }

    /**
     * Gets the set of pieces that are currently threatened
     *
     * @return the set of pieces that are currently threatened
     */
    public Set<ChessPiece> getThreatenedPieces() {
        var threatened = new HashSet<ChessPiece>();
        var currentSideToGo = board.getSideToMove();

        var currentPositions = chessPieces.stream()
                .filter(ChessPiece::isOnTheBoard)
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
     *
     * @param piece         piece to have AID attached to it
     * @param pieceAgentAID AID of agent representing that piece
     */
    public void registerAgentPiece(ChessPiece piece, OntoAID pieceAgentAID) {
        var agentPiece = chessPieces.stream().filter(p -> p.equals(piece)).findFirst().get();
        // remove
        chessPieces.remove(agentPiece);
        // set AID
        agentPiece.setAgentAID(pieceAgentAID);
        // recalculate hash using AID instead of position, type, and colour
        chessPieces.add(agentPiece);
    }

    public Set<ChessPiece> getAllPieces() {
        return chessPieces;
    }

    public Set<ChessPiece> getPiecesFiltered(Collection<Predicate<ChessPiece>> filterPredicates) {
        var compositePredicate = filterPredicates.stream().reduce(w -> true, Predicate::and);
        return chessPieces.stream().filter(compositePredicate).collect(Collectors.toSet());
    }

    public boolean gameIsOver() {
        return board.isMated() || board.isDraw() || board.isStaleMate() || board.isInsufficientMaterial();
    }

    public Optional<ChessPiece> getPieceAtPosition(Position position) {
        return getAllPieces().stream().filter(ChessPiece::isOnTheBoard).filter(p -> p.getPosition().equals(position)).findFirst();
    }

    public Set<ChessPiece> getCapturedPieces() {
        return capturedPieces;
    }

    public Set<PieceMove> getAllLegalMoves() {
        try {
            return MoveGenerator.generateLegalMoves(board).stream().map(ChessBoard::constructPieceMove).collect(Collectors.toSet());
        } catch (MoveGeneratorException e) {
            return Collections.emptySet();
        }
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
