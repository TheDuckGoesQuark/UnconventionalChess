package chessagents.agents.pieceagent.planner;

import chessagents.chess.ChessBoard;
import chessagents.ontology.schemas.concepts.Piece;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GameState {

    private final Set<Piece> livePieces;
    private final Piece observer;
    private final ChessBoard chessboard;
    private final Set<Piece> threatenedPieces;
    private final Set<Piece> capturedPieces;

    public GameState(Piece observer, Set<Piece> livePieces) {
        this.livePieces = livePieces;
        this.observer = observer;
        this.chessboard = new ChessBoard();
        threatenedPieces = new HashSet<>();
        capturedPieces = new HashSet<>();
    }

    private GameState(Piece observer, Set<Piece> livePieces, ChessBoard board, Set<Piece> capturedPieces) {
        this.observer = observer;
        this.livePieces = livePieces;
        this.chessboard = board;
        this.capturedPieces = capturedPieces;
        this.threatenedPieces = board.getThreatenedPieces(livePieces);
    }

    /**
     * Apply the given action to a copy of the current game state, and return the copy
     *
     * @param pieceAction action to apply
     * @return copy of this game state with the given action applied
     */
    public GameState apply(PieceAction pieceAction) {
        var newBoard = pieceAction.getMove().map(chessboard::copyOnMove).orElse(chessboard);

        // if board is equal just return this state. board may be equal if attempting to perform an invalid move
        // or if no move is actually performed as part of this action
        if (newBoard.equals(chessboard)) return this;
        else {
            var newCaptured = new HashSet<>(capturedPieces);
            var newLive = new HashSet<>(livePieces);

            pieceAction.getCapturedPiece().ifPresent(piece -> {
                newLive.remove(piece);
                newCaptured.add(piece);
            });

            return new GameState(observer, newLive, newBoard, newCaptured);
        }
    }

    /**
     * @return the set of pieces the same colour as the observer that are currently threatened
     */
    public Set<Piece> getFriendlyThreatenedPieces() {
        return threatenedPieces.stream()
                .filter(p -> p.getColour().equals(observer.getColour()))
                .collect(Collectors.toSet());
    }

    /**
     * @return the set of enemies the opposite colour as the observer that have been captured
     */
    public Set<Piece> getEnemiesCaptured() {
        return capturedPieces.stream()
                .filter(p -> !p.getColour().equals(observer.getColour()))
                .collect(Collectors.toSet());
    }
}
