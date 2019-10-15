package chessagents.agents.commonbehaviours;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Simple behaviour for replying not understood to any message
 */
public class ReplyNotUnderstood extends OneShotBehaviour {

    private final ACLMessage message;

    public ReplyNotUnderstood(ACLMessage message) {
        this.message = message;
    }

    @Override
    public void action() {
        final ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        myAgent.send(reply);
    }
}
