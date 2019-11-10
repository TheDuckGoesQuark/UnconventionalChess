package chessagents.agents.pieceagent.eventhandlers;

import chessagents.agents.pieceagent.events.FailedToMoveEvent;
import chessagents.agents.pieceagent.pieces.PieceAgent;

public class FailedToMoveEventHandler extends PieceEventHandler<FailedToMoveEvent> {

    public void apply(FailedToMoveEvent event, PieceAgent agent) {
        // do nothing
    }
}
