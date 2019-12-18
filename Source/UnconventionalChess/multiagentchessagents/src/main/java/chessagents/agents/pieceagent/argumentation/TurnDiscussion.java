package chessagents.agents.pieceagent.argumentation;

import java.util.LinkedList;

public class TurnDiscussion {

    private final LinkedList<MoveDiscussion> moveDiscussions = new LinkedList<>();

    public void handleMessage(ConversationMessage conversationMessage) {

    }

    public int getNumberOfMessages() {
        return moveDiscussions.stream()
                .map(MoveDiscussion::getNumberOfMessages)
                .reduce(0, Integer::sum);
    }
}
