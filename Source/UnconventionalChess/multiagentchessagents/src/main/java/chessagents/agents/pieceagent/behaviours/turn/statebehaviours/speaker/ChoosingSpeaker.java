package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker.RequestSpeakerProposals.SPEAKER_CONTRACT_NET_PROTOCOL;

public class ChoosingSpeaker extends PieceStateBehaviour {

    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private final Set<ACLMessage> speakerProposals = new HashSet<>();
    private MessageTemplate mt = null;

    public ChoosingSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.CHOOSING_SPEAKER);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    protected void initialiseState() {
        speakerProposals.clear();

        var conversationID = turnContext.getCurrentMessage().getConversationId();

        mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(SPEAKER_CONTRACT_NET_PROTOCOL),
                MessageTemplate.and(
                        MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                        MessageTemplate.MatchConversationId(conversationID)
                )
        );
    }

    @Override
    public void action() {
        if (receivedRequestFromEveryone()) {
            var speaker = chooseSpeaker();
            sendResults(speaker);

            // Clear speaker to avoid confusion in future states
            turnContext.setCurrentSpeaker(null);
            setEvent(PieceTransition.SPEAKER_CHOSEN);
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
        return ((PieceAgent) myAgent).chooseSpeaker(speakerProposals);
    }

    private boolean receivedRequestFromEveryone() {
        var numAgents = pieceContext.getGameContext().getAllPieceAgentAIDs().size();
        logger.info("Received request to speak from " + speakerProposals.size() + "/" + numAgents + " of agents");
        logger.info("Waiting for: ");
        var received = speakerProposals.stream().map(ACLMessage::getSender).collect(Collectors.toSet());
        pieceContext.getGameContext().getAllPieceAgentAIDs().stream()
                .filter(a -> !received.contains(a))
                .forEach(a -> logger.info(a.getLocalName()));

        return speakerProposals.size() == numAgents;
    }
}
