package chessagents.agents.pieceagent.planner.goal;

import chessagents.agents.pieceagent.planner.GameState;
import chessagents.agents.pieceagent.planner.PieceAction;

/**
 * A goal is a game state that can be achieved
 */
public abstract class Value {

    private final String name;

    Value(String name) {
        this.name = name;
    }

    public abstract boolean actionMaintainsValue(GameState gameState, PieceAction pieceAction);

    /**
     * Get the name of this goal
     *
     * @return the name of this goal
     */
    public String getName() {
        return name;
    }
}
