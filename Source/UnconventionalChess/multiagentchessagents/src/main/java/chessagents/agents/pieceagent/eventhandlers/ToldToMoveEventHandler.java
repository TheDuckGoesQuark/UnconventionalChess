package chessagents.agents.pieceagent.eventhandlers;

import chessagents.agents.pieceagent.events.ToldToMoveEvent;
import chessagents.agents.pieceagent.pieces.PieceAgent;

public class ToldToMoveEventHandler extends PieceEventHandler<ToldToMoveEvent> {

    public void apply(ToldToMoveEvent event, PieceAgent agent) {
        // do nothing
    }
}
