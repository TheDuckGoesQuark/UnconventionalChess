package chessagents.agents.commonbehaviours;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Simple behaviour for replying not understood to any message
 */
public class Reply extends OneShotBehaviour {

    private final ACLMessage message;
    private final int performative;

    public Reply(ACLMessage message, int performative) {
        this.message = message;
        this.performative = performative;
    }

    @Override
    public void action() {
        final ACLMessage reply = message.createReply();
        reply.setPerformative(performative);
        myAgent.send(reply);
    }
}
