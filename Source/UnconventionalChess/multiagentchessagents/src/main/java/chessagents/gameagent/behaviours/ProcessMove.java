package chessagents.gameagent.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProcessMove extends CyclicBehaviour {

    private MessageTemplate messageTemplate;

    @Override
    public void action() {
        ACLMessage message = myAgent.receive();

        if (message != null && message.getPerformative() == ACLMessage.PROPOSE) {
            reply(message);
        } else {
            System.out.println("No move to process");
            block();
        }
    }

    private void reply(ACLMessage message) {
        String content = message.getContent();
        System.out.println("Move: " + content);

        final ACLMessage reply = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        reply.setSender(myAgent.getAID());
        reply.addReceiver(message.getSender());
        reply.setContent(message.getContent());
        reply.setConversationId(message.getConversationId());

        System.out.println("Sending reply");
        myAgent.send(reply);
        System.out.println("reply sent");
    }
}
