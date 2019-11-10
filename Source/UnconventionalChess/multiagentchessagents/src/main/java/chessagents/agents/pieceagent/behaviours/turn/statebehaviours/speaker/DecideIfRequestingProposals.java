package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;

import java.util.Random;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.NOT_REQUESTING_PROPOSALS;
import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.REQUESTING_PROPOSALS;

public class DecideIfRequestingProposals extends PieceStateBehaviour {

    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public DecideIfRequestingProposals(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.DECIDE_IF_REQUESTING_PROPOSALS);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        if (turnContext.getDebateCycles() < pieceContext.getMaxDebateCycle() && requestingProposals()) {
            setEvent(REQUESTING_PROPOSALS);
        } else {
            setEvent(NOT_REQUESTING_PROPOSALS);
        }
    }

    private boolean requestingProposals() {
        return ((PieceAgent) myAgent).requestingProposals();
    }
}
