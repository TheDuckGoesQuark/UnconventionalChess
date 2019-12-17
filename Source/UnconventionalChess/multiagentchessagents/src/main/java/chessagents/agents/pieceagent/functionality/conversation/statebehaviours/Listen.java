package chessagents.agents.pieceagent.functionality.conversation.statebehaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.functionality.conversation.ConversationContext;
import chessagents.agents.pieceagent.functionality.conversation.ConversationState;
import chessagents.agents.pieceagent.functionality.conversation.ConversationTransition;

public class Listen extends ConversationStateBehaviour {
    public Listen(PieceAgent pieceAgent, ConversationContext conversationContext) {
        super(pieceAgent, ConversationState.LISTEN, conversationContext);
    }

    @Override
    public void action() {
        // TODO wait for confirmation message
        // listen flowchart
        setTransition(ConversationTransition.LISTENED);
    }
}
