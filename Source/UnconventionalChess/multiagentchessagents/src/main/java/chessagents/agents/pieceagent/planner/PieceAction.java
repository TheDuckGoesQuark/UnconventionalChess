package chessagents.agents.pieceagent.planner;

import chessagents.ontology.schemas.concepts.Move;
import chessagents.ontology.schemas.concepts.Piece;

import java.util.Optional;

public abstract class PieceAction {

    private String name;
    private Piece actor;

    protected PieceAction(String name, Piece actor) {
        this.name = name;
        this.actor = actor;
    }

    /**
     * Gets the piece that performed this action
     *
     * @return piece that performed this action
     */
    public Piece getActor() {
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
     * Gets the move if the action involves making one
     *
     * @return move made as part of this action
     */
    public Optional<Move> getMove() {
        return Optional.empty();
    }

    /**
     * Gets the piece that is captured as a result of this action if any
     *
     * @return the piece captured as part of this action
     */
    public Optional<Piece> getCapturedPiece() {
        return Optional.empty();
    }
}
