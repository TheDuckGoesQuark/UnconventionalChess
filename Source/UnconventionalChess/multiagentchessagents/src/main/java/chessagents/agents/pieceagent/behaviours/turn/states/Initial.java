package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;
import jade.util.Logger;

public class Initial extends SimpleBehaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private PieceTransition nextTransition = null;

    public Initial(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void onStart() {
        logCurrentState(logger, PieceState.INITIAL);
        nextTransition = null;
        turnContext.reset();
    }

    @Override
    public void action() {
        nextTransition = pieceContext.isMyTurnToGo() ? PieceTransition.MY_TURN : PieceTransition.NOT_MY_TURN;
    }

    @Override
    public boolean done() {
        return nextTransition != null;
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }

    @Override
    public int getNextTransition() {
        return nextTransition.ordinal();
    }
}
