package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.REQUESTED_TO_SPEAK;

public class RequestToSpeak extends OneShotBehaviour implements PieceStateBehaviour {
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public RequestToSpeak(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public int getNextTransition() {
        return REQUESTED_TO_SPEAK.ordinal();
    }

    @Override
    public void action() {
        logger.info("Requesting to speak");
        var cfp = turnContext.getCurrentMessage();
        var proposal = ((PieceAgent) myAgent).constructProposalToSpeak(cfp);
        myAgent.send(proposal);
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
