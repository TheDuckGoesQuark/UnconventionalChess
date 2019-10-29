package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameContext;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder;

public class InformSubscribersOfMoves extends SimpleBehaviour {
    private GameContext context;

    public InformSubscribersOfMoves(GameContext context) {
        this.context = context;
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }

    public void addSubscriber(SubscriptionResponder.Subscription sub) {
    }

    public void removeSubscriber(ACLMessage cancel) {
    }
}
