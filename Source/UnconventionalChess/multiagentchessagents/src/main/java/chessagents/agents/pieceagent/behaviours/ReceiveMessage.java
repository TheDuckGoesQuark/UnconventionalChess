package chessagents.agents.pieceagent.behaviours;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveMessage extends SimpleBehaviour {

    private MessageTemplate template;
    private long timeout, wakeupTime;
    private boolean receivedResponse;
    private ACLMessage msg;

    public ReceiveMessage(Agent a, int millis, MessageTemplate mt) {
        super(a);
        timeout = millis;
        template = mt;
    }

    @Override
    public void onStart() {
        wakeupTime = (timeout < 0 ? Long.MAX_VALUE
                : System.currentTimeMillis() + timeout);
    }

    @Override
    public void action() {
        if (template == null)
            msg = myAgent.receive();
        else
            msg = myAgent.receive(template);

        if (msg != null) {
            receivedResponse = true;
            handle(msg);
            return;
        }

        long dt = wakeupTime - System.currentTimeMillis();

        if (dt > 0)
            block();
        else {
            receivedResponse = true;
            handle(msg);
        }
    }

    public void handle(ACLMessage message) {
    }

    public void reset() {
        msg = null;
        receivedResponse = false;
        super.reset();
    }

    public void reset(int dt) {
        timeout = dt;
        reset();
    }

    @Override
    public boolean done() {
        return receivedResponse;
    }
}
