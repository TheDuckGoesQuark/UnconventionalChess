package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.argumentation.ConversationMessage;

public interface ConversationAction {

    ConversationMessage perform();

    default String grammarTag() {
        return "<" + getClass().getSimpleName() + ">";
    }

}
