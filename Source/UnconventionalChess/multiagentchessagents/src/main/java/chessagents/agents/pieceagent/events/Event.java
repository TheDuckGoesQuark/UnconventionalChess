package chessagents.agents.pieceagent.events;

import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;

/**
 * Events are transitions that occurred and any information relevant to the transition
 */
public class Event {

    private PieceTransition transition;

    public Event(PieceTransition transition) {
        this.transition = transition;
    }

    public PieceTransition getTransition() {
        return transition;
    }

}
