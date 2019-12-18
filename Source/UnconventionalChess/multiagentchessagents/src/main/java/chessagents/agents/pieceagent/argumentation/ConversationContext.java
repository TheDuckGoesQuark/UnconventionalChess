package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.pieceagent.PieceAgent;
import jade.content.OntoAID;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

public class ConversationContext {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final ConversationPlanner conversationPlanner;
    private int turnCounter = 0;
    private int speakerRotationCounter = 0;
    private OntoAID speaker;

    public ConversationContext(PieceAgent pieceAgent) {
        conversationPlanner = new ConversationPlannerImpl(pieceAgent);
    }

    public String getConversationID() {
        return String.format("conversation-%d-%d", turnCounter, speakerRotationCounter);
    }

    public MessageTemplate getConversationIdMatcher() {
        return MessageTemplate.MatchConversationId(getConversationID());
    }

    public OntoAID getSpeaker() {
        return speaker;
    }

    public void setSpeaker(OntoAID speaker) {
        this.speaker = speaker;
        speakerRotationCounter++;
    }

    public void handleConversationMessage(ConversationMessage conversationMessage) {
        conversationPlanner.handleConversationMessage(conversationMessage);
    }

    public ConversationMessage produceMessage() {
        return conversationPlanner.produceMessage();
    }

    public void startNewTurn() {
        turnCounter++;
        speakerRotationCounter = 0;
        speaker = null;
        conversationPlanner.startNewTurn();
    }
}
