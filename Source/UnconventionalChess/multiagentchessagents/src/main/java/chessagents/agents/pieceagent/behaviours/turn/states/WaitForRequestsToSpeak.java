package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.Behaviour;

public class WaitForRequestsToSpeak extends Behaviour implements PieceStateBehaviour {

    public WaitForRequestsToSpeak(PieceAgent pieceAgent, PieceAgent pieceAgent1, TurnContext turnContext) {
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public int getNextTransition() {
        return 0;
    }

    @Override
    public void reset() {
        super.reset();
    }
}
