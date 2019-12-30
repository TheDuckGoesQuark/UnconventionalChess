package chessagents.agents.pieceagent.behaviours.conversation.statebehaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationContext;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationState;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationTransition;
import chessagents.util.RandomUtil;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ChooseSpeaker extends ConversationStateBehaviour {

    private Set<ACLMessage> speakerProposals = new HashSet<>();
    private MessageTemplate mt;

    public ChooseSpeaker(PieceAgent a, ConversationContext conversationContext) {
        super(a, ConversationState.CHOOSE_SPEAKER, conversationContext);
    }

    @Override
    public void onStart() {
        super.onStart();
        speakerProposals.clear();
        mt = MessageTemplate.and(
                getConversationContext().getConversationIdMatcher(),
                MessageTemplate.MatchProtocol(StartSpeakerElection.SPEAKER_ELECTION_PROTOCOL_NAME)
        );
    }

    @Override
    public void action() {
        if (receivedRequestFromEveryone()) {
            var nextSpeaker = chooseSpeaker(speakerProposals);
            sendResults(nextSpeaker);
            setTransition(ConversationTransition.SPEAKER_CHOSEN);
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

    private AID chooseSpeaker(Set<ACLMessage> speakerProposals) {
        var move = getConversationContext().getLastMoveDiscussed();
        var myAID = getAgent().getAID();

        if (move != null) {
            // allow piece being asked to move to talk
            var pieceMoving = getAgent().getPieceContext().getGameState().getPieceAtPosition(move.getSource());
            var pieceMovingAID = pieceMoving.get().getAgentAID();

            // if that piece was me, I've already spoke so chose someone else
            if (!pieceMovingAID.equals(myAID)) {
                return pieceMovingAID;
            }
        }

        // choose random other piece. if im the only piece that can speak then choose from anyone
        var possibleSpeakers = speakerProposals.stream().filter(s -> !s.getSender().equals(myAID)).collect(Collectors.toSet());
        if (possibleSpeakers.size() == 0) {
            return new RandomUtil<ACLMessage>().chooseRandom(speakerProposals).getSender();
        } else {
            return new RandomUtil<ACLMessage>().chooseRandom(possibleSpeakers).getSender();
        }
    }

    private boolean receivedRequestFromEveryone() {
        var pieceContext = getAgent().getPieceContext();
        var myPiece = pieceContext.getMyPiece();
        var numAgents = pieceContext.getGameState().getAllAgentPiecesForColourOnBoard(myPiece.getColour()).size();
        return speakerProposals.size() == numAgents;
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
}
