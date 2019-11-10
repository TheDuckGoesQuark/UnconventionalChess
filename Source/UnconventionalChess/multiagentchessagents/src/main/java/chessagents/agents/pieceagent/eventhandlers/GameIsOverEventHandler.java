package chessagents.agents.pieceagent.eventhandlers;

import chessagents.agents.pieceagent.events.GameIsOverEvent;
import chessagents.agents.pieceagent.pieces.PieceAgent;

public class GameIsOverEventHandler extends PieceEventHandler<GameIsOverEvent> {

    public void apply(GameIsOverEvent event, PieceAgent agent) {
        // do nothing
    }
}
