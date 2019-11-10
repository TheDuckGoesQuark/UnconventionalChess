package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.MOVE_PERFORMED;

public class PerformMove extends OneShotBehaviour implements PieceStateBehaviour {
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public PerformMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void onStart() {
        logCurrentState(logger, PieceState.PERFORM_MOVE);
    }

    @Override
    public void action() {
        var move = turnContext.getCurrentMove();

        if (move != null) {
            pieceContext.makeMove(move.getSource(), move.getTarget());
            logger.info("Performed move " + move.getSource().getCoordinates() + ":" + move.getTarget().getCoordinates());
        } else {
            logger.warning("UNABLE TO MAKE MOVE, MOVE WAS NULL");
        }
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }

    @Override
    public int getNextTransition() {
        return MOVE_PERFORMED.ordinal();
    }
}
