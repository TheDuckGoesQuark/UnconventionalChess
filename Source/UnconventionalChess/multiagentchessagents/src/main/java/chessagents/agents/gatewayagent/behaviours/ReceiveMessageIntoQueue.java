package chessagents.agents.gatewayagent.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Queue;

public class ReceiveMessageIntoQueue extends OneShotBehaviour {

    private final Queue<ACLMessage> messageQueue;

    public ReceiveMessageIntoQueue(Queue<ACLMessage> messageQueue) {
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
