package chessagents.agents.pieceagent.events;

import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;

/**
 * transitions that occurred and any information relevant to the transition
 */
public class TransitionEvent {

    private PieceTransition transition;

    public TransitionEvent(PieceTransition transition) {
        this.transition = transition;
    }

    public PieceTransition getTransition() {
        return transition;
    }

}
