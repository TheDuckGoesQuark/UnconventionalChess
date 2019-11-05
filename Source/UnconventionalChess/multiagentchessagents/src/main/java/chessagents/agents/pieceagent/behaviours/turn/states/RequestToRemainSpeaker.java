package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.tools.sniffer.Message;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.REQUESTED_TO_REMAIN_SPEAKER;
import static chessagents.agents.pieceagent.behaviours.turn.states.RequestSpeakerProposals.SPEAKER_CONTRACT_NET_PROTOCOL;

public class RequestToRemainSpeaker extends SimpleBehaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchProtocol(SPEAKER_CONTRACT_NET_PROTOCOL),
            MessageTemplate.MatchSender(myAgent.getAID())
    );
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private boolean done = false;

    public RequestToRemainSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void onStart() {
        done = false;
    }

    @Override
    public int getNextTransition() {
        return REQUESTED_TO_REMAIN_SPEAKER.ordinal();
    }

    @Override
    public void action() {
        var message = myAgent.receive(mt);

        if (message != null) {
            logger.info("Received CFP to self");
            turnContext.setCurrentMessage(message);
            var proposal = ((PieceAgent) myAgent).constructProposalToSpeak(message);
            myAgent.send(proposal);
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return done;
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
