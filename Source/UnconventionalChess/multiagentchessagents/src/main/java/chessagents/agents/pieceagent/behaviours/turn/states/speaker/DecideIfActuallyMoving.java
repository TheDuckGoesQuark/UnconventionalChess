package chessagents.agents.pieceagent.behaviours.turn.states.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.states.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.ACTUALLY_MOVING;

public class DecideIfActuallyMoving extends SimpleBehaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());

    public DecideIfActuallyMoving(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
    }

    @Override
    public void onStart() {
        logCurrentState(logger, PieceState.DECIDE_IF_ACTUALLY_MOVING);
    }

    @Override
    public void action() {
        // TODO implement
        // TODO send failure to everyone if not actually moving
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int getNextTransition() {
        return ACTUALLY_MOVING.ordinal();
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
