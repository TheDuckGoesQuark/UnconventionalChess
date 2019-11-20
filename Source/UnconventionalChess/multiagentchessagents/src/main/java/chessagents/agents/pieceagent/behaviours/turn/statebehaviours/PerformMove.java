package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.actions.PerformMoveAction;
import jade.util.Logger;

public class PerformMove extends PieceStateBehaviour {
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public PerformMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.PERFORM_MOVE);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var move = turnContext.getCurrentMove();

        if (move != null) {
            pieceContext.getGameState().makeMove(move);
            logger.info("Performed move " + move.getSource().getCoordinates() + ":" + move.getTarget().getCoordinates());
            setChosenAction(new PerformMoveAction(pieceContext.getPieceForAID(getAgent().getAID()).get()));
        } else {
            logger.warning("UNABLE TO MAKE MOVE, MOVE WAS NULL");
        }
    }
}
