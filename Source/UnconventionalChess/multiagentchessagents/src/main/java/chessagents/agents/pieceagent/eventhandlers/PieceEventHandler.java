package chessagents.agents.pieceagent.eventhandlers;

import chessagents.agents.pieceagent.events.Event;
import chessagents.agents.pieceagent.pieces.PieceAgent;

public interface PieceEventHandler<E> {

    /**
     * Applies the given event to the given agent
     *
     * @param e     event to apply
     * @param agent agent experiencing the event
     */
    default void apply(Event e, PieceAgent agent) {
        // do nothing
    }

}
