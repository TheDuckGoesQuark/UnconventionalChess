package chessagents.agents.pieceagent.argumentation;

public interface ConversationPlanner {

    void handleConversationMessage(ConversationMessage conversationMessage);

    ConversationMessage produceMessage();

}
