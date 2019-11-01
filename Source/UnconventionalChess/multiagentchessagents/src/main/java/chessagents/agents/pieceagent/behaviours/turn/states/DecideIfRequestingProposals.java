package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.NOT_REQUESTING_PROPOSALS;

public class DecideIfRequestingProposals extends SimpleBehaviour {

    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public DecideIfRequestingProposals(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        // TODO actual logic for deciding if requesting proposals or telling piece to move
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int onEnd() {
        return NOT_REQUESTING_PROPOSALS.ordinal();
    }

    @Override
    public void reset() {
        super.reset();
    }
}
