package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.nonspeaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.REQUESTED_TO_SPEAK;

public class RequestToSpeak extends SimpleBehaviour implements PieceStateBehaviour {
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public RequestToSpeak(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void onStart() {
        logCurrentState(logger, PieceState.REQUEST_TO_SPEAK);
    }

    @Override
    public int getNextTransition() {
        return REQUESTED_TO_SPEAK.ordinal();
    }

    @Override
    public void action() {
        var cfp = turnContext.getCurrentMessage();
        var proposal = ((PieceAgent) myAgent).constructProposalToSpeak(cfp);
        myAgent.send(proposal);
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
