package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.PROPOSALS_REQUESTED;

public class RequestSpeakerProposals extends OneShotBehaviour implements PieceStateBehaviour {

    public static final String SPEAKER_CONTRACT_NET_PROTOCOL = "SPEAKER_CONTRACT_NET_PROTOCOL";
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public RequestSpeakerProposals(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public int getNextTransition() {
        return PROPOSALS_REQUESTED.ordinal();
    }

    @Override
    public void action() {
        requestProposals();
    }

    private void requestProposals() {
        var cfp = ChessMessageBuilder.constructMessage(ACLMessage.CFP);

        // send to everyone (including myself!)
        pieceContext.getGameContext().getAllPieceAgentAIDs().forEach(cfp::addReceiver);
        cfp.setProtocol(SPEAKER_CONTRACT_NET_PROTOCOL);

        // Store CFP
        turnContext.setCurrentMessage(cfp);

        myAgent.send(cfp);
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
