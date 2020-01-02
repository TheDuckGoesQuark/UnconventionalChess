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
        this.previousState = getLastEntry();
        return super.put(transitioningMove, updatedState);
    }

    private GameState getLastEntry() {
        var iter = this.entrySet().iterator();
        Map.Entry<PieceMove, GameState> current = null;
        while (iter.hasNext()) current = iter.next();

        return current != null ? current.getValue() : null;
    }

    public PieceMove getLastMove() {
        return lastMove;
    }

    public GameState getPreviousState() {
        return previousState;
    }
}
