package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.TurnDiscussion;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AskForProposals implements ConversationAction {

    private final PieceAgent pieceAgent;
    private final TurnDiscussion turnDiscussion;

    @Override
    public ConversationMessage perform() {
        return createAskForProposalMessage();
    }

    private ConversationMessage createAskForProposalMessage() {
        return new ConversationMessage("Any ideas what we should do next?", null, pieceAgent.getAID());
    }
}
