package chessagents.agents.pieceagent.behaviours.conversation.statebehaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationContext;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationState;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationTransition;
import chessagents.agents.pieceagent.behaviours.conversation.SendChatMessage;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.lang.acl.ACLMessage;
import rita.wordnet.jwnl.wndata.Exc;

import java.io.IOException;

public class Speak extends ConversationStateBehaviour {

    private static final long SPEAK_DELAY = 200;

    public Speak(PieceAgent a, ConversationContext conversationContext) {
        super(a, ConversationState.SPEAK, conversationContext);
    }

    @Override
    public void action() {
        var message = getConversationContext().produceMessage();

        syntheticDelay();

        if (!message.movePerformed()) {
            setTransition(ConversationTransition.SPOKE);
        } else {
            setTransition(ConversationTransition.MAKING_MOVE);
        }

        sendChat(message.getStatement());
        sendToAllMyAgents(message);
    }

    private void sendToAllMyAgents(ConversationMessage conversationMessage) {
        var aclMessage = new ACLMessage(ACLMessage.INFORM);
        aclMessage.setConversationId(getConversationContext().getConversationID());

        var pieceContext = getAgent().getPieceContext();
        // add all pieces other than me as receivers
        pieceContext.getGameState()
                .getAllAgentPiecesForColour(pieceContext.getMyPiece().getColour())
                .stream()
                .map(ChessPiece::getAgentAID)
                .filter(a -> !a.equals(myAgent.getAID()))
                .forEach(aclMessage::addReceiver);

        try {
            aclMessage.setContentObject(conversationMessage);
        } catch (IOException e) {
            logger.warning("Failed to serialize conversation message");
        }

        myAgent.send(aclMessage);
    }

    private void sendChat(String message) {
        var myAgent = getAgent();
        var myAID = myAgent.getAID();
        var gameAgentAID = myAgent.getPieceContext().getGameAgentAID();
        var chatBehaviour = new SendChatMessage(message, myAID, gameAgentAID);
        myAgent.addBehaviour(chatBehaviour);
    }

    private void syntheticDelay() {
        try {
            logger.info("sleeping...");
            Thread.sleep(SPEAK_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
