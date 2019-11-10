package chessagents.agents.pieceagent.eventhandlers;

import chessagents.agents.pieceagent.events.NotMyTurnEvent;
import chessagents.agents.pieceagent.pieces.PieceAgent;

public class NotMyTurnEventHandler extends PieceEventHandler<NotMyTurnEvent> {

    public void apply(NotMyTurnEvent event, PieceAgent agent) {
        // do nothing
    }
}
