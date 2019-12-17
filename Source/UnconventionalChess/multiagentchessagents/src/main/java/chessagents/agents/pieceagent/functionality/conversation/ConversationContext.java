package chessagents.agents.pieceagent.functionality.conversation;

import jade.content.OntoAID;
import jade.lang.acl.MessageTemplate;

public class ConversationContext {

    private int roundCounter = 0;
    private int turnCounter = 0;
    private OntoAID speaker;

    public String getConversationID() {
        return String.format("conversation-%d-%d", turnCounter, roundCounter);
    }

    public MessageTemplate getConversationIdMatcher() {
        return MessageTemplate.MatchConversationId(getConversationID());
    }

    public OntoAID getSpeaker() {
        return speaker;
    }

    public void setSpeaker(OntoAID speaker) {
        this.speaker = speaker;
        turnCounter++;
    }

    public void startNewTurn() {
        roundCounter++;
        speaker = null;
    }
}
