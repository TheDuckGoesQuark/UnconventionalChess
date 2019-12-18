package chessagents.agents.pieceagent.behaviours.conversation.statebehaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationContext;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationState;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationTransition;
import chessagents.agents.pieceagent.behaviours.conversation.SendChatMessage;

public class Speak extends ConversationStateBehaviour {

    private static final long SPEAK_DELAY = 3000;

    public Speak(PieceAgent a, ConversationContext conversationContext) {
        super(a, ConversationState.SPEAK, conversationContext);
    }

    @Override
    public void action() {
        syntheticDelay();
        var conversationMessage = getConversationContext().getConversationPlanner().produceMessage();
        sendChat(conversationMessage.getAsHumanFriendlyString());
        setTransition(ConversationTransition.SPOKE);
    }

    private void syntheticDelay() {
        try {
            logger.info("sleeping...");
            Thread.sleep(SPEAK_DELAY);
            myAgent.addBehaviour(new SendChatMessage("wassup, " + getConversationContext().getConversationID(), myAgent.getAID(), getAgent().getPieceContext().getGameAgentAID()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendChat(String message) {
        var myAgent = getAgent();
        var myAID = myAgent.getAID();
        var gameAgentAID = myAgent.getPieceContext().getGameAgentAID();
        var chatBehaviour = new SendChatMessage(message, myAID, gameAgentAID);
        myAgent.addBehaviour(chatBehaviour);
    }
}
