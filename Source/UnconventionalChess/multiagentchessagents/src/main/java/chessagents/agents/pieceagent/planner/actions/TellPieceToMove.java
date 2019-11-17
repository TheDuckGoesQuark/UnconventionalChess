package chessagents.agents.pieceagent.planner.actions;

import chessagents.agents.pieceagent.planner.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;

import java.util.Optional;

public class TellPieceToMove extends PieceAction {
    private final PieceMove move;
    private final ChessPiece otherChessPiece;

    protected TellPieceToMove(ChessPiece actor, PieceMove move, ChessPiece otherChessPiece) {
        super(pieceTransition, "Tell piece to move", actor);
        this.move = move;
        this.otherChessPiece = otherChessPiece;
    }

    @Override
    public ChessPiece getActor() {
        return null;
    }

    @Override
    public Optional<PieceMove> getMove() {
        return Optional.of(move);
    }

    /*
     * TODO READ THIS AND DO THIS YOU SCHMUK
     *
     * Each piece state transition behaviour call PieceAgent.chooseNextAction(Set<Action>):Action and provides a set
     * of possible next actions (i.e. choose x as speaker, tell x to move, make move x)
     *
     * Planner checks if any of the possible actions will help attain the goal, and returns whatever action
     * does so best.
     *
     * If none, random?
     *
     * Once the agent chooses its action (or another agent receives knowledge of the action that was chosen), they
     * call PieceAgent.performAction(Action):PieceTransition which will update their game state accordingly,
     * and provide the next transition as an outcome of the action.
     */
}
