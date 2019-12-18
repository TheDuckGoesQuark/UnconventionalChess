package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.pieceagent.PieceAgent;

public class ConversationPlannerImpl implements ConversationPlanner {

    private final PieceAgent agent;

    public ConversationPlannerImpl(PieceAgent pieceAgent) {
        this.agent = pieceAgent;
    }

    @Override
    public void handleConversationMessage(ConversationMessage conversationMessage) {

    }

    @Override
    public ConversationMessage produceMessage() {
        return null;
    }
}
