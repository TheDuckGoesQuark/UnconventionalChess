package chessagents.agents.pieceagent.functionality.conversation;

import jade.content.OntoAID;
import jade.lang.acl.MessageTemplate;

import java.util.UUID;

public class ConversationContext {

    private String conversationID;
    private MessageTemplate conversationIdMatcher;
    private OntoAID speaker;

    public ConversationContext() {
        generateNewConversationID();
    }

    private void generateNewConversationID() {
        conversationID = UUID.randomUUID().toString();
        conversationIdMatcher = MessageTemplate.MatchConversationId(conversationID);
    }

    public String getConversationID() {
        return conversationID;
    }

    public MessageTemplate getConversationIdMatcher() {
        return conversationIdMatcher;
    }

    public OntoAID getSpeaker() {
        return speaker;
    }

    public void setSpeaker(OntoAID speaker) {
        this.speaker = speaker;
    }

    public void reset() {
        generateNewConversationID();
        speaker = null;
    }
}
