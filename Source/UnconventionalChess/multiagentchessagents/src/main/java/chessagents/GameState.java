package chessagents;

import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.chess.ChessBoard;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.ontology.schemas.concepts.Position;

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
        return getAllPieces().stream()
                .filter(p -> p.getAgentAID() != null)
                .filter(p -> p.getColour().equals(colour))
                .collect(Collectors.toSet());
    }

    /**
     * Finds the piece that is at the given position
     *
     * @param position position to find piece for
     * @return piece if one exists at that position, empty otherwise
     */
    public Optional<ChessPiece> getPieceAtPosition(Position position) {
        return board.getPieceAtPosition(position);
    }

    /**
     * Registers the given pieces as agents
     *
     * @param pieces set of pieces with AIDs populated
     */
    public void registerPiecesAsAgents(Set<ChessPiece> pieces) {
        pieces.forEach(p -> board.registerAgentPiece(p, p.getAgentAID()));
    }

    /**
     * Makes the given move, returning the new game state after the move is made
     *
     * @param move move to make
     * @return game state after move is made
     */
    public GameState makeMove(PieceMove move) {
        return new GameState(gameId, board.copyOnMove(move));
    }

    public boolean isSideToGo(Colour colour) {
        return board.isSideToGo(colour);
    }

    /**
     * Apply the given action to a copy of the current game state, and return the copy
     *
     * @param pieceAction action to apply
     * @return copy of this game state with the given action applied
     */
    public GameState apply(PieceAction pieceAction) {
        return pieceAction.performOnStateOnly(this);
    }

    public Set<ChessPiece> getCapturedForColour(Colour colour) {
        return board.getCapturedPieces().stream()
                .filter(p -> p.getColour().equals(colour))
                .collect(Collectors.toSet());
    }

    public Optional<PieceMove> getRandomMove() {
        return board.getRandomMove();
    }

    public Colour getSideToGo() {
        return board.getSideToGo();
    }

    public Set<ChessPiece> getThreatenedPieces() {
        return board.getThreatenedPieces();
    }

    public Set<PieceMove> getAllLegalMoves() {
        return board.getAllLegalMoves();
    }
}
