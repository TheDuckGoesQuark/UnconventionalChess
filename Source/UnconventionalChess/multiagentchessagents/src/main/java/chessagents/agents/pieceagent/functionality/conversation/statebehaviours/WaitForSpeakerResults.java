package chessagents.agents.pieceagent.functionality.conversation.statebehaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.functionality.conversation.ConversationContext;
import chessagents.agents.pieceagent.functionality.conversation.ConversationState;
import chessagents.agents.pieceagent.functionality.conversation.ConversationTransition;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForSpeakerResults extends ConversationStateBehaviour {

    private MessageTemplate mt;

    public WaitForSpeakerResults(PieceAgent a, ConversationContext conversationContext) {
        super(a, ConversationState.WAIT_FOR_SPEAKER_RESULTS, conversationContext);
    }

    @Override
    public void onStart() {
        super.onStart();
        mt = MessageTemplate.and(
                getConversationContext().getConversationIdMatcher(),
                MessageTemplate.MatchProtocol(StartSpeakerElection.SPEAKER_ELECTION_PROTOCOL_NAME)
        );
    }

    @Override
    public void action() {
        // TODO wait for either accept or reject from current speaker
        var message = myAgent.receive(mt);

        if (message != null) {
            if (message.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                getConversationContext().setSpeaker(getAgent().getPieceContext().getMyAID());
            } else {
                getConversationContext().setSpeaker(getAgent().getPieceContext().getMyAID());
            }

            setTransition(ConversationTransition.SPEAKER_CONFIRMED);
        } else {
            block();
        }
    }
}
