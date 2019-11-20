package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.actions.AskForProposalsAction;
import chessagents.agents.pieceagent.actions.TryTellPieceToMoveAction;

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
        setChosenAction(pieceContext.chooseAction(createPossibleActions(turnContext.getDebateCycles() < pieceContext.getMaxDebateCycle())));
    }

    private Set<PieceAction> createPossibleActions(boolean canDebateFurther) {
        var possibleActions = new HashSet<PieceAction>();

        var me = pieceContext.getPieceForAID(myAgent.getAID()).get();

        if (canDebateFurther) {
            // we could ask everyone what move we should make
            possibleActions.add(new AskForProposalsAction(me));
        }

        // we try could tell any of the other pieces to move (or oneself!)
        possibleActions.add(new TryTellPieceToMoveAction(me));

        return possibleActions;
    }
}
