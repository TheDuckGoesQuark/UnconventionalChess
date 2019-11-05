package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;

import java.util.Random;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.NOT_REQUESTING_PROPOSALS;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.REQUESTING_PROPOSALS;

public class DecideIfRequestingProposals extends OneShotBehaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final Random random = new Random();
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private PieceTransition pieceTransition = null;

    public DecideIfRequestingProposals(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void onStart() {
        pieceTransition = null;
    }

    @Override
    public void action() {
        if (turnContext.getDebateCycles() < pieceContext.getMaxDebateCycle() && requestingProposals()) {
            pieceTransition = REQUESTING_PROPOSALS;
        } else {
            pieceTransition = NOT_REQUESTING_PROPOSALS;
        }
    }

    private boolean requestingProposals() {
        // TODO change from random chance to personality affected decision
        return random.nextBoolean();
    }

    @Override
    public int getNextTransition() {
        return (pieceTransition != null ? pieceTransition : NOT_REQUESTING_PROPOSALS).ordinal();
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
