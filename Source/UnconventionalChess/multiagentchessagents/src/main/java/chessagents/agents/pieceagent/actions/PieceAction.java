package chessagents.agents.pieceagent.actions;

import chessagents.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.util.Logger;

public abstract class PieceAction {

    protected final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceTransition resultingTransition;
    private final String name;

    /**
     * @param resultingTransition the transition to be taken if this action is chosen
     * @param name                name of this action
     * @param actor               piece performing the action
     */
    protected PieceAction(PieceTransition resultingTransition, String name, ChessPiece actor) {
        this.resultingTransition = resultingTransition;
        this.name = name;
    }

    /**
     * Gets the name of this action
     *
     * @return name of this action
     */
    public String getActionName() {
        return name;
    }

    /**
     * Gets the transition that should be taken once this action is performed
     *
     * @return the transition that should be taken once this action is performed
     */
    public PieceTransition getTransition() {
        return resultingTransition;
    }

    /**
     * Agent performs given action according to current game state with all side effects such as sending messages
     * to other agents
     *
     * @param actor     agent that will perform the action
     * @param gameState current game state
     * @return game state after action has been performed
     */
    public abstract GameState perform(PieceAgent actor, GameState gameState);

    /**
     * Applies the action to the game state so that decisions can be made based on the outcomes of actions, but
     * doesn't execute any side effects such as sending messages to other agents.
     *
     * @param gameState current game state
     * @return game state after action has been performed
     */
    public abstract GameState getOutcomeOfAction(GameState gameState);
}
