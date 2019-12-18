package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.pieceagent.PieceAgent;
import jade.content.OntoAID;
import jade.lang.acl.MessageTemplate;

public class ConversationContext {

    private final ConversationPlanner conversationPlanner;
    private int roundCounter = 0;
    private int turnCounter = 0;
    private OntoAID speaker;

    public ConversationContext(PieceAgent pieceAgent) {
        conversationPlanner = new ConversationPlannerImpl(pieceAgent);
    }

    public String getConversationID() {
        return String.format("conversation-%d-%d", turnCounter, roundCounter);
    }

    public ConversationPlanner getConversationPlanner() {
        return conversationPlanner;
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
