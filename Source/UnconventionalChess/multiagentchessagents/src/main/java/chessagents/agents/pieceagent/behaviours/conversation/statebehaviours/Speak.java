package chessagents.agents.pieceagent.behaviours.conversation.statebehaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationContext;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationState;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationTransition;
import chessagents.agents.pieceagent.behaviours.conversation.SendChatMessage;

public class Speak extends ConversationStateBehaviour {

    private static final long speakDelay = 3000;

    public Speak(PieceAgent a, ConversationContext conversationContext) {
        super(a, ConversationState.SPEAK, conversationContext);
    }

    @Override
    public void action() {
        // speaker flow chart
        try {
            logger.info("sleeping...");
            Thread.sleep(1000);
            myAgent.addBehaviour(new SendChatMessage("wassup, " + getConversationContext().getConversationID(), myAgent.getAID(), getAgent().getPieceContext().getGameAgentAID()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTransition(ConversationTransition.SPOKE);
    }
}
