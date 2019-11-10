package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.events.Event;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.Behaviour;
import jade.util.Logger;

public class GameOver extends PieceStateBehaviour {

    public GameOver(PieceAgent pieceAgent) {
        super(pieceAgent, PieceState.GAME_OVER);
    }

    @Override
    public void action() {
        // TODO something?
        setEvent(PieceTransition.GAME_IS_OVER);
    }
}
