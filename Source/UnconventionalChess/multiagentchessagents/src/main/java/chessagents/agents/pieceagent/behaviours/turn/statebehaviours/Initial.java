package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.PieceAgent;

public class Initial extends PieceStateBehaviour {

    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public Initial(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.INITIAL);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    protected void initialiseState() {
        turnContext.reset();
    }

    @Override
    public void action() {
        setEvent(pieceContext.isMyTurnToGo() ? PieceTransition.MY_TURN : PieceTransition.NOT_MY_TURN);
    }
}
