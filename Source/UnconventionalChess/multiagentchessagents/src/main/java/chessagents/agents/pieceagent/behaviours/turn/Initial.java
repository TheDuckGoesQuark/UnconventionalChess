package chessagents.agents.pieceagent.behaviours.turn;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;
import jade.util.Logger;

public class Initial extends SimpleBehaviour {

    public static final PieceState STATE = PieceState.INITIAL;
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext context;
    private PieceTransition nextTransition = null;

    public Initial(PieceAgent pieceAgent, PieceContext context) {
        super(pieceAgent);
        this.context = context;
    }

    @Override
    public void action() {
        nextTransition = context.isMyTurnToGo() ? PieceTransition.MY_TURN : PieceTransition.NOT_MY_TURN;
    }

    @Override
    public boolean done() {
        return nextTransition != null;
    }

    @Override
    public void reset() {
        nextTransition = null;
        super.reset();
    }

    @Override
    public int onEnd() {
        return nextTransition.ordinal();
    }
}
