package chessagents.agents.pieceagent.behaviours.turn.states;

import jade.core.behaviours.Behaviour;

public class GameOver extends Behaviour implements PieceStateBehaviour {
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
