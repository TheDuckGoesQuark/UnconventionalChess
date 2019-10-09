package chessagents.agents.gameagent.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ReceiveMessages extends CyclicBehaviour {
    @Override
    public void action() {
        final ACLMessage msg = getAgent().receive();

        if (msg == null) {
            block();
            return;
        }

        // DO something
    }
}
