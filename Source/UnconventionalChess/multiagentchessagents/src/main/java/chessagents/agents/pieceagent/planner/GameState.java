package chessagents.agents.pieceagent.planner;

import chessagents.chess.ChessBoard;
import chessagents.ontology.schemas.concepts.ChessPiece;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GameState {

    private final Set<ChessPiece> liveChessPieces;
    private final ChessPiece observer;
    private final ChessBoard chessboard;
    private final Set<ChessPiece> threatenedChessPieces;
    private final Set<ChessPiece> capturedChessPieces;

    public GameState(ChessPiece observer, Set<ChessPiece> liveChessPieces) {
        this.liveChessPieces = liveChessPieces;
        this.observer = observer;
        this.chessboard = new ChessBoard();
        threatenedChessPieces = new HashSet<>();
        capturedChessPieces = new HashSet<>();
    }

    private GameState(ChessPiece observer, Set<ChessPiece> liveChessPieces, ChessBoard board, Set<ChessPiece> capturedChessPieces) {
        this.observer = observer;
        this.liveChessPieces = liveChessPieces;
        this.chessboard = board;
        this.capturedChessPieces = capturedChessPieces;
        this.threatenedChessPieces = board.getThreatenedPieces(liveChessPieces);
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
            var newCaptured = new HashSet<>(capturedChessPieces);
            var newLive = new HashSet<>(liveChessPieces);

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
    public Set<ChessPiece> getFriendlyThreatenedPieces() {
        return threatenedChessPieces.stream()
                .filter(p -> p.getColour().equals(observer.getColour()))
                .collect(Collectors.toSet());
    }

    /**
     * @return the set of enemies the opposite colour as the observer that have been captured
     */
    public Set<ChessPiece> getEnemiesCaptured() {
        return capturedChessPieces.stream()
                .filter(p -> !p.getColour().equals(observer.getColour()))
                .collect(Collectors.toSet());
    }
}
