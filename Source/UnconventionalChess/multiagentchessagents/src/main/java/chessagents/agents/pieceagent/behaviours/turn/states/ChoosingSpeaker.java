package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static chessagents.agents.pieceagent.behaviours.turn.states.RequestSpeakerProposals.SPEAKER_CONTRACT_NET_PROTOCOL;

public class ChoosingSpeaker extends Behaviour implements PieceStateBehaviour {

    private static final MessageTemplate mt = MessageTemplate.MatchProtocol(SPEAKER_CONTRACT_NET_PROTOCOL);
    private final Random random = new Random();
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private final Set<ACLMessage> speakerProposals = new HashSet<>();
    private PieceTransition transition = null;

    public ChoosingSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        if (receivedRequestFromEveryone()) {
            var speaker = chooseSpeaker();
            sendResults(speaker);
        } else {
            // receive all messages for this protocol
            var message = myAgent.receive(mt);

            if (message != null) {
                speakerProposals.add(message);
            } else {
                block();
            }
        }
    }

    private void sendResults(AID speaker) {
        speakerProposals.stream()
                .map(proposal -> constructReply(proposal, speaker))
                .forEach(myAgent::send);
    }

    private ACLMessage constructReply(ACLMessage proposal, AID speaker) {
        var reply = proposal.createReply();
        var receiver = reply.getAllReceiver().next();

        if (receiver.equals(speaker)) {
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            addReplyToAllRejected(reply, speaker);
        } else {
            reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
        }

        return reply;
    }

    private void addReplyToAllRejected(ACLMessage reply, AID speaker) {
        // inform everyone else when they've became the speaker
        speakerProposals.stream()
                .map(ACLMessage::getSender)
                .filter(aid -> !aid.equals(speaker))
                .forEach(reply::addReplyTo);
    }

    private AID chooseSpeaker() {
        var arr = speakerProposals.toArray(new ACLMessage[0]);
        return arr[random.nextInt(arr.length)].getSender();
    }

    private boolean receivedRequestFromEveryone() {
        return speakerProposals.size() == (pieceContext.getGameContext().getAllPieceAgentAIDs().size());
    }

    @Override
    public boolean done() {
        return transition != null;
    }

    @Override
    public int getNextTransition() {
        return transition.ordinal();
    }

    @Override
    public void reset() {
        transition = null;
        speakerProposals.clear();
        super.reset();
    }

    @Override
    public int onEnd() {
        turnContext.setCurrentSpeaker(null);
        return getNextTransition();
    }
}
