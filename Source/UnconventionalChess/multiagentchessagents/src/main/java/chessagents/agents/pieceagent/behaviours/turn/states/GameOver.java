package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import jade.core.behaviours.Behaviour;
import jade.util.Logger;

public class GameOver extends Behaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());

    @Override
    public void onStart() {
        logCurrentState(logger, PieceState.END_TURN);
    }

    @Override
    public void action() {
        // TODO something?
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int getNextTransition() {
        // Special case, no transitions
        return 0;
    }
}
