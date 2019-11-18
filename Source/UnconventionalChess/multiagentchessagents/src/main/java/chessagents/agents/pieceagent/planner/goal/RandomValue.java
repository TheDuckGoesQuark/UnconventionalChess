package chessagents.agents.pieceagent.planner.goal;

import chessagents.GameState;
import chessagents.agents.pieceagent.planner.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;

import java.util.Random;

public class RandomValue extends Value {

    private static final Random random = new Random();

    public RandomValue() {
        super("No value, random!");
    }

    @Override
    public boolean actionMaintainsValue(ChessPiece pieceWithValue, GameState gameState, PieceAction pieceAction) {
        return random.nextBoolean();
    }
}
