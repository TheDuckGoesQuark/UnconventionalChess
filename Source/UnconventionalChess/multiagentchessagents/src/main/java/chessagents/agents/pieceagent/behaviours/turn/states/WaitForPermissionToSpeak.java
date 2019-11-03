package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.Behaviour;

public class WaitForPermissionToSpeak extends Behaviour implements PieceStateBehaviour {
    public WaitForPermissionToSpeak(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
    }

    @Override
    public int getNextTransition() {
        return 0;
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }
}
