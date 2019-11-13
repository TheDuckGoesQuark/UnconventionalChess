package chessagents.agents.pieceagent.planner.goal;

import chessagents.agents.pieceagent.planner.GameState;
import chessagents.agents.pieceagent.planner.PieceAction;

import java.util.Random;

public class RandomValue extends Value {

    private static final Random random = new Random();

    public RandomValue() {
        super("No value");
    }

    @Override
    public boolean actionMaintainsValue(GameState gameState, PieceAction pieceAction) {
        return random.nextBoolean();
    }
}
