package chessagents.agents.commonbehaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Generic behaviour for receiving messages continuously
 */
public abstract class CyclicReceiveMessage extends CyclicBehaviour {

    private final MessageTemplate template;

    public CyclicReceiveMessage() {
        this.template = null;
    }

    public CyclicReceiveMessage(MessageTemplate mt) {
        super();
        template = mt;
    }

    @Override
    public void action() {
        final ACLMessage msg;

        if (template == null)
            msg = myAgent.receive();
        else
            msg = myAgent.receive(template);

        if (msg != null) {
            handle(msg);
            return;
        }

        block();
    }

    public abstract void handle(ACLMessage message);
}
