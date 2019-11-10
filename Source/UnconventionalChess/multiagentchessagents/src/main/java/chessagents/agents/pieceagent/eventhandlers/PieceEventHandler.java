package chessagents.agents.pieceagent.eventhandlers;

import chessagents.agents.pieceagent.events.Event;
import chessagents.agents.pieceagent.pieces.PieceAgent;

public abstract class PieceEventHandler<E extends Event> {

    /**
     * Applies the given event to the given agent
     *
     * @param e     event to apply
     * @param agent agent experiencing the event
     */
    public void apply(E e, PieceAgent agent) {
        // do nothing
    }
}
