package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.TurnDiscussion;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProposeMove implements ConversationAction {
    private final PieceAgent pieceAgent;
    private final TurnDiscussion turnDiscussion;
    @Override
    public ConversationMessage perform() {
        return null;
    }
}