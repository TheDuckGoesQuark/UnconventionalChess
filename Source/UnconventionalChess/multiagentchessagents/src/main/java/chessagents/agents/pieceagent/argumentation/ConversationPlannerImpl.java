package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.pieceagent.PieceAgent;

import java.util.LinkedList;
import java.util.List;

public class ConversationPlannerImpl implements ConversationPlanner {

    /**
     * Agent that this planner is for
     */
    private final PieceAgent agent;
    /**
     * Discussions during each turn, with first move at first index
     */
    private final List<TurnDiscussion> turnDiscussions = new LinkedList<>();

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

    @Override
    public void startNewTurn() {
        turnDiscussions.add(new TurnDiscussion());
    }
}
