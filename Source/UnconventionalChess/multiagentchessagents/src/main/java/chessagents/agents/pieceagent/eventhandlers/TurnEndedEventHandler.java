package chessagents.agents.pieceagent.eventhandlers;

import chessagents.agents.pieceagent.events.TurnEndedEvent;
import chessagents.agents.pieceagent.pieces.PieceAgent;

public class TurnEndedEventHandler extends PieceEventHandler<TurnEndedEvent> {

    public void apply(TurnEndedEvent event, PieceAgent agent) {
        // do nothing
    }
}
