package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.PieceAgent;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.MOVE_PERFORMED;

public class PerformMove extends PieceStateBehaviour {
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public PerformMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.PERFORM_MOVE);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var move = turnContext.getCurrentMove();

        if (move != null) {
            pieceContext.getGameState().makeMove(move);
            logger.info("Performed move " + move.getSource().getCoordinates() + ":" + move.getTarget().getCoordinates());
            setEvent(MOVE_PERFORMED);
        } else {
            logger.warning("UNABLE TO MAKE MOVE, MOVE WAS NULL");
        }
    }
}
