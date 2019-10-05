package moveprocessor.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProcessMove extends CyclicBehaviour {

    private MessageTemplate messageTemplate;

    @Override
    public void action() {
        System.out.println("Processing move");

        messageTemplate = MessageTemplate.MatchConversationId("sending-move");
        ACLMessage message = myAgent.receive();

        if (message != null) {
            if (message.getPerformative() == ACLMessage.REQUEST) {
                String content = message.getContent();
                System.out.println("Move: " + content);
            }
        } else {
            System.out.println("No move to process");
            block();
        }
    }
}
