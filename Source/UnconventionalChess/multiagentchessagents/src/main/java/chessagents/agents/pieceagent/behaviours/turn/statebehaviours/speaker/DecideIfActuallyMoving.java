package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.ACTUALLY_MOVING;
import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.FAILED_TO_MOVE;

public class DecideIfActuallyMoving extends PieceStateBehaviour {

    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public DecideIfActuallyMoving(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.DECIDE_IF_ACTUALLY_MOVING);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var pieceAgent = getAgent();

        if (pieceAgent.isActuallyMoving()) {
            // Give piece opportunity to actually perform different move
            // TODO commented out until able to fetch next move from plan
//            turnContext.setCurrentMove(pieceAgent.getNextMove());
            setEvent(ACTUALLY_MOVING);
        } else {
            // TODO send failure to everyone if not actually moving
            setEvent(FAILED_TO_MOVE);
        }
    }
}
