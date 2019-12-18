package chessagents.agents.pieceagent.behaviours.conversation.statebehaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationContext;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationState;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationTransition;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.Optional;

public class Listen extends ConversationStateBehaviour {

    private MessageTemplate mt = null;

    public Listen(PieceAgent pieceAgent, ConversationContext conversationContext) {
        super(pieceAgent, ConversationState.LISTEN, conversationContext);
    }

    @Override
    public void onStart() {
        super.onStart();
        var conversationContext = getConversationContext();
        mt = MessageTemplate.and(
                MessageTemplate.MatchSender(conversationContext.getSpeaker()),
                MessageTemplate.MatchConversationId(conversationContext.getConversationID())
        );
    }

    @Override
    public void action() {
        var message = myAgent.receive(mt);

        if (message != null) {
            var contents = extractContents(message);

            if (contents.isPresent()) {
                var planner = getConversationContext().getConversationPlanner();
                planner.handleConversationMessage(contents.get());
                setTransition(ConversationTransition.LISTENED);
            }
        } else {
            block();
        }
    }

    private Optional<ConversationMessage> extractContents(ACLMessage message) {
        Optional<ConversationMessage> conversationMessage;

        try {
            conversationMessage = Optional.ofNullable((ConversationMessage) message.getContentObject());
        } catch (UnreadableException e) {
            logger.warning("Failed to extract object from message: " + e.getMessage());
            conversationMessage = Optional.empty();
        }

        return conversationMessage;
    }
}
