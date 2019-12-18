package chessagents.agents.pieceagent.behaviours.conversation.statebehaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationContext;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationState;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationTransition;

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
