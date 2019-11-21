package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.NoAction;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.actions.PieceAction;

import java.util.HashSet;
import java.util.Set;

public class DecideIfRequestingProposals extends PieceStateBehaviour {

    private final TurnContext turnContext;

    public DecideIfRequestingProposals(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.DECIDE_IF_REQUESTING_PROPOSALS);
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        setChosenAction(getAgent().chooseAction(createPossibleActions(turnContext.getDebateCycles() < pieceContext.getMaxDebateCycle())));
    }

    private Set<PieceAction> createPossibleActions(boolean canDebateFurther) {
        var possibleActions = new HashSet<PieceAction>();

        var me = getMyPiece();

        if (canDebateFurther) {
            // TODO actions like this could have an unknown outcome, we should account for that?
            // we could ask everyone what move we should make
            possibleActions.add(new NoAction(PieceTransition.REQUESTING_PROPOSALS, "Decide to request proposals", me));
        }

        // we try could tell any of the other pieces to move (or oneself!)
        // (TODO during the get outcome method for this action, we should provide the side effects of all the moves so
        // this gets evaluated better
        possibleActions.add(new NoAction(PieceTransition.NOT_REQUESTING_PROPOSALS, "Decide to try and make a move happen", me));

        return possibleActions;
    }
}
