package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.NOT_REQUESTING_PROPOSALS;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.REQUESTED_PROPOSALS;

public class DecideIfRequestingProposals extends SimpleBehaviour {

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
    public void action() {
        if (turnContext.getDebateCycles() < pieceContext.getMaxDebateCycle() && requestingProposals()) {
            pieceTransition = REQUESTED_PROPOSALS;
            requestProposals();
        } else {
            pieceTransition = NOT_REQUESTING_PROPOSALS;
        }
    }

    private void requestProposals() {
        var request = ((ChessAgent) myAgent).constructMessage(ACLMessage.CFP);
    }

    private boolean requestingProposals() {
        // TODO change from random chance to personality affected decision
        return random.nextBoolean();
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int onEnd() {
        return (pieceTransition != null ? pieceTransition : NOT_REQUESTING_PROPOSALS).ordinal();
    }

    @Override
    public void reset() {
        pieceTransition = null;
        super.reset();
    }
}
