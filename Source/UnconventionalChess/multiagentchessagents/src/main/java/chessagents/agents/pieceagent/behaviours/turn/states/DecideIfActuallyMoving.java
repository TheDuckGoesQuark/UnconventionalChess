package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.ACTUALLY_MOVING;

public class DecideIfActuallyMoving extends SimpleBehaviour {
    public DecideIfActuallyMoving(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int onEnd() {
        return ACTUALLY_MOVING.ordinal();
    }
}
