package chessagents.agents.pieceagent.goals;

import chessagents.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
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
