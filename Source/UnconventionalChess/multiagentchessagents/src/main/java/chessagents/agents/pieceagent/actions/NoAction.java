package chessagents.agents.pieceagent.actions;

import chessagents.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class NoAction extends PieceAction {
    /**
     * @param resultingTransition the transition to be taken if this action is chosen
     * @param name                name of this action
     * @param actor               piece performing the action
     */
    public NoAction(PieceTransition resultingTransition, String name, ChessPiece actor) {
        super(resultingTransition, name, actor);
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        return performOnStateOnly(gameState);
    }

    @Override
    public GameState performOnStateOnly(GameState gameState) {
        return gameState;
    }
}
