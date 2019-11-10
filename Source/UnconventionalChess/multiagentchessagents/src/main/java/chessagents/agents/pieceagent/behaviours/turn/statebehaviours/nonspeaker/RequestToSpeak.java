package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.nonspeaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.REQUESTED_TO_SPEAK;

public class RequestToSpeak extends PieceStateBehaviour {
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public RequestToSpeak(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.REQUEST_TO_SPEAK);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var cfp = turnContext.getCurrentMessage();
        var proposal = ((PieceAgent) myAgent).constructProposalToSpeak(cfp);
        myAgent.send(proposal);
        setEvent(REQUESTED_TO_SPEAK);
    }
}
