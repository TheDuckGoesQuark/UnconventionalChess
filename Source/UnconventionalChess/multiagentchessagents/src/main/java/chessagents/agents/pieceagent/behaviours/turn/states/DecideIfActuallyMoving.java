package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.ACTUALLY_MOVING;

public class DecideIfActuallyMoving extends SimpleBehaviour implements PieceStateBehaviour {
    public DecideIfActuallyMoving(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
    }

    @Override
    public void action() {
        // TODO implement
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int getNextTransition() {
        return ACTUALLY_MOVING.ordinal();
    }
}
