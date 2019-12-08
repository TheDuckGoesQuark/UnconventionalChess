package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.NoAction;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.PieceAgent;

public class Initial extends PieceStateBehaviour {

    private final PieceAction myTurnAction;
    private final PieceAction notMyTurnAction;
    private final TurnContext turnContext;

    public Initial(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.INITIAL);
        this.turnContext = turnContext;
        this.myTurnAction = new NoAction(PieceTransition.MY_TURN, "My Turn", getMyPiece());
        this.notMyTurnAction = new NoAction(PieceTransition.NOT_MY_TURN, "Not My Turn", getMyPiece());
    }

    @Override
    protected void initialiseState() {
        turnContext.reset();
    }

    @Override
    public void action() {
        setChosenAction(getNextAction());
    }

    private PieceAction getNextAction() {
        if (pieceContext.getGameState().gameIsOver()) {
            return new NoAction(PieceTransition.GAME_IS_OVER, "Game is over", getMyPiece());
        } else if (getMyPiece().isOnTheBoard()) {
            return pieceContext.isMyTurnToGo() ? myTurnAction : notMyTurnAction;
        } else {
            return new NoAction(PieceTransition.IM_CAPTURED, "I'm captured", getMyPiece());
        }
    }

}
