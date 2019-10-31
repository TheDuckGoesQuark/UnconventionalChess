package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.MOVE_PERFORMED;

public class PerformMove extends OneShotBehaviour {
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public PerformMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var move = turnContext.getCurrentMove();
        var from = move.getSource().getCoordinates();
        var to = move.getTarget().getCoordinates();
        pieceContext.getBoard().makeMove(from, to);
        logger.info("Performed move " + from + to);
    }

    @Override
    public int onEnd() {
        return MOVE_PERFORMED.ordinal();
    }
}
