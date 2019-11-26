package chessagents.agents.pieceagent.goals;

import chessagents.chess.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;

/**
 * A goal is a game state that can be achieved
 */
public abstract class Value {

    private final String name;

    Value(String name) {
        this.name = name;
    }

    public abstract boolean actionMaintainsValue(ChessPiece pieceWithValue, GameState gameState, PieceAction pieceAction);

    /**
     * Get the name of this goal
     *
     * @return the name of this goal
     */
    public String getName() {
        return name;
    }

}
