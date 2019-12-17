package chessagents.agents.pieceagent.goals;

import chessagents.agents.pieceagent.ActionResponse;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;

/**
 * A goal is a game state that can be achieved
 */
public abstract class Value {

    private final String name;

    Value(String name) {
        this.name = name;
    }

    /**
     * Gets the given pieces response to the action applied to the given game state, with whether or not
     * it approves of the action and the reasoning for its decision
     *
     * @param chessPiece piece considering the action
     * @param gameState  game state
     * @param action     action being considered
     * @return the given pieces response to the action applied to the given game state, with whether or not
     * it approves of the action and the reasoning for its decision
     */
    public abstract ActionResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action);

    /**
     * Get the name of this goal
     *
     * @return the name of this goal
     */
    public String getName() {
        return name;
    }


}
