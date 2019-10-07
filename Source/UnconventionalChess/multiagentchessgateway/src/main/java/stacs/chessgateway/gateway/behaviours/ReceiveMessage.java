package stacs.chessgateway.gateway.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;


public class ReceiveMessage extends OneShotBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(ReceiveMessage.class);
    private final Queue<ACLMessage> messageQueue;

    public ReceiveMessage(Queue<ACLMessage> messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive();

        if (message != null) {
            messageQueue.add(message);
        } else {
            block();
        }
    }
}
