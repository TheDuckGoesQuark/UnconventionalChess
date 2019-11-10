package chessagents.agents.pieceagent.eventhandlers;

import chessagents.agents.pieceagent.events.NotMovingEvent;
import chessagents.agents.pieceagent.pieces.PieceAgent;

public class NotMovingEventHandler extends PieceEventHandler<NotMovingEvent> {

    public void apply(NotMovingEvent event, PieceAgent agent) {
        // do nothing
    }
}
