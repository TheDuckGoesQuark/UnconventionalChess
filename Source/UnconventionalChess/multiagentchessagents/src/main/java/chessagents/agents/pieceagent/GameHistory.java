package chessagents.agents.pieceagent;

import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.PieceMove;
import com.sun.jdi.connect.spi.TransportService;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class GameHistory {

    private LinkedList<GameState> states = new LinkedList<>();
    private LinkedList<PieceMove> moves = new LinkedList<>();

    public GameState put(PieceMove transitioningMove, GameState updatedState) {
        this.moves.add(transitioningMove);
        this.states.add(updatedState);
        return updatedState;
    }

    public PieceMove getLastMove() {
        return moves.getLast();
    }

    public GameState getPreviousState() {
        if (states.size() < 2) return null;
        else return states.get(states.size() - 2);
    }

    public GameState getCurrentState() {
        return states.getLast();
    }
}
