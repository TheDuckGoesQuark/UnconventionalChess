package chessagents.agents.pieceagent;

import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.PieceMove;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameHistory extends LinkedHashMap<PieceMove, GameState> {

    private PieceMove lastMove;
    private GameState previousState = null;

    @Override
    public GameState put(PieceMove transitioningMove, GameState updatedState) {
        this.lastMove = transitioningMove;
        this.previousState = getCurrentState();
        return super.put(transitioningMove, updatedState);
    }

    public PieceMove getLastMove() {
        return lastMove;
    }

    public GameState getPreviousState() {
        return previousState;
    }

    public GameState getCurrentState() {
        return this.getLastEntry();
    }

    private GameState getLastEntry() {
        var iter = this.entrySet().iterator();
        Map.Entry<PieceMove, GameState> current = null;
        while (iter.hasNext()) current = iter.next();

        return current != null ? current.getValue() : null;
    }

}
