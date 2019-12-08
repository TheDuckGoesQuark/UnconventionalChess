package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.NoAction;
import chessagents.agents.pieceagent.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.PieceAgent;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.TURN_ENDED;

public class EndTurn extends PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final TurnContext turnContext;

    public EndTurn(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.END_TURN);
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        logger.info("Turn ended!");
        setChosenAction(new NoAction(TURN_ENDED, "End Turn", getMyPiece()));
    }
}
