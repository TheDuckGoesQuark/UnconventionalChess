package chessagents.chess;

import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.ontology.schemas.concepts.Position;
import jade.content.OntoAID;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GameState {

    private final ChessBoard board;
    private final int gameId;

    public GameState(int gameId) {
        this(gameId, new ChessBoard());
    }

    private GameState(int gameId, ChessBoard board) {
        this.board = board;
        this.gameId = gameId;
    }

    /**
     * Retrieves the game ID
     *
     * @return the game ID
     */
    public int getGameId() {
        return gameId;
    }


    /**
     * Returns the set of all pieces
     *
     * @return the set of all pieces
     */
    public Set<ChessPiece> getAllPieces() {
        return board.getAllPieces();
    }

    /**
     * Retrieves the set of all pieces that are currently represented by agents for the given colour
     *
     * @param colour colour of pieces to return
     * @return set of all pieces of the given colour that are controlled by agents
     */
    public Set<ChessPiece> getAllAgentPiecesForColour(Colour colour) {
        return board.getPiecesFiltered(List.of(
                PieceFilter.isAgent(),
                PieceFilter.isColour(colour)
        ));
    }

    /**
     * Retrieves the piece that is represented by an agent with the given AID
     *
     * @param aid the AID to look for
     * @return the piece that is represented by an agent with the given aid
     */
    public Optional<ChessPiece> getAgentPieceWithAID(OntoAID aid) {
        var iter = board.getPiecesFiltered(List.of(
                PieceFilter.hasAID(aid)
        )).iterator();

        return iter.hasNext() ? Optional.ofNullable(iter.next()) : Optional.empty();
    }

    /**
     * Finds the piece that is at the given position
     *
     * @param position position to find piece for
     * @return piece if one exists at that position, empty otherwise
     */
    public Optional<ChessPiece> getPieceAtPosition(Position position) {
        var iter = board.getPiecesFiltered(List.of(
                PieceFilter.isAtPosition(position)
        )).iterator();

        return iter.hasNext() ? Optional.ofNullable(iter.next()) : Optional.empty();
    }

    public Set<ChessPiece> getAllPiecesForTypeAndColour(String pieceTypeName, Colour colour) {
        return board.getPiecesFiltered(List.of(
                PieceFilter.isType(pieceTypeName),
                PieceFilter.isColour(colour)
        ));
    }

    public Set<ChessPiece> getAllAgentPiecesForColourOnBoard(Colour colour) {
        return board.getPiecesFiltered(List.of(
                PieceFilter.isAgent(),
                PieceFilter.isNotCaptured(),
                PieceFilter.isColour(colour)
        ));
    }

    /**
     * Registers the given pieces as agents
     *
     * @param pieces set of pieces with AIDs populated
     */
    public void registerPiecesAsAgents(Set<ChessPiece> pieces) {
        pieces.forEach(p -> board.registerAgentPiece(p, p.getAgentAID()));
    }

    public void registerPieceAtPositionAsAgent(OntoAID aid, Position position) {
        board.registerAgentPiece(getPieceAtPosition(position).get(), aid);
    }

    /**
     * Makes the given move, returning the new game state after the move is made
     *
     * @param move move to make
     * @return game state after move is made
     */
    public GameState applyMove(PieceMove move) {
        return new GameState(gameId, board.copyOnMove(move));
    }

    public boolean isSideToGo(Colour colour) {
        return board.isSideToGo(colour);
    }

    public Set<ChessPiece> getCapturedForColour(Colour colour) {
        return board.getCapturedPieces().stream()
                .filter(p -> p.getColour().equals(colour))
                .collect(Collectors.toSet());
    }

    public Set<ChessPiece> getThreatenedPieces() {
        return board.getThreatenedPieces();
    }

    public Set<ChessPiece> getThreatenedForColour(Colour colour) {
        return board.getThreatenedPieces().stream()
                .filter(p -> p.getColour().equals(colour))
                .collect(Collectors.toSet());
    }

    public Set<PieceMove> getAllLegalMoves() {
        return board.getAllLegalMoves();
    }

    public boolean gameIsOver() {
        return board.gameIsOver();
    }

    public boolean isValidMove(PieceMove move) {
        return board.isValidMove(move);
    }

    public Set<ChessPiece> getAllPiecesOnBoard() {
        return board.getPiecesFiltered(List.of(
                PieceFilter.isNotCaptured()
        ));
    }
}
