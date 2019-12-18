package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;

public class AskForProposals implements ConversationAction {

    private final PieceAgent pieceAgent;

    public AskForProposals(PieceAgent pieceAgent) {
        this.pieceAgent = pieceAgent;
    }

    @Override
    public ConversationMessage perform() {
        return createAskForProposalMessage();
    }

    private ConversationMessage createAskForProposalMessage() {
        return new ConversationMessage("Any ideas what we should do next?", null, pieceAgent.getAID());
    }
}
