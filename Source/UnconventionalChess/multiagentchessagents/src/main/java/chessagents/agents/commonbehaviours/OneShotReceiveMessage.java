package chessagents.agents.commonbehaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Generic behaviour for one-off receiving messages.
 * Will be called until a message is received and then returns
 */
public abstract class OneShotReceiveMessage extends SimpleBehaviour {

    private final MessageTemplate template;
    private boolean received = false;

    public OneShotReceiveMessage() {
        this.template = null;
    }

    public OneShotReceiveMessage(MessageTemplate mt) {
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
            received = true;
            handle(msg);
            return;
        }

        block();
    }

    public abstract void handle(ACLMessage message);

    @Override
    public boolean done() {
        return received;
    }
}
