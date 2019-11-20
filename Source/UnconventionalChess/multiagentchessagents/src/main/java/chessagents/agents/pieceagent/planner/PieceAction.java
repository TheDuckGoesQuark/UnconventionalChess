package chessagents.agents.pieceagent.planner;

import chessagents.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.util.Logger;

import java.util.Optional;

public abstract class PieceAction {

    protected final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceTransition resultingTransition;
    private final String name;
    private final ChessPiece actor;

    /**
     * @param resultingTransition the transition to be taken if this action is chosen
     * @param name                name of this action
     * @param actor               piece performing the action
     */
    protected PieceAction(PieceTransition resultingTransition, String name, ChessPiece actor) {
        this.resultingTransition = resultingTransition;
        this.name = name;
        this.actor = actor;
    }

    /**
     * Gets the piece that performed this action
     *
     * @return piece that performed this action
     */
    public ChessPiece getActor() {
        return actor;
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
     * Gets the move if the action involves making one
     *
     * @return move made as part of this action
     */
    public Optional<PieceMove> getMove() {
        return Optional.empty();
    }

    /**
     * Gets the piece that is captured as a result of this action if any
     *
     * @return the piece captured as part of this action
     */
    public Optional<ChessPiece> getCapturedPiece() {
        return Optional.empty();
    }

    /**
     * Agent performs given action according to current game state
     *
     * @param actor     agent that will perform the action
     * @param gameState current game state
     */
    public abstract void perform(PieceAgent actor, GameState gameState);
}
