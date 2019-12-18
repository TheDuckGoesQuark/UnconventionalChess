package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AgreeToPerformMove implements ConversationAction {
    private final TurnDiscussion turnDiscussion;
    private final PieceAgent pieceAgent;
    @Override
    public ConversationMessage perform() {
        return null;
    }
}
