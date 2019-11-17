package chessagents.agents.pieceagent.planner;

import chessagents.ontology.schemas.concepts.ChessPiece;

import java.util.Set;

/**
 * Tracks previous actions and events and
 */
public class Planner {

    private History history = new History();
    private GameState gameState;

    public Planner(ChessPiece observer, Set<ChessPiece> liveChessPieces) {
        this.gameState = new GameState(observer, liveChessPieces);
    }

    public void performAction(PieceAction action) {
        var newState = gameState.apply(action);

        if (!newState.equals(gameState)) {
            this.history.add(gameState);
            this.gameState = newState;
        }
    }
}
