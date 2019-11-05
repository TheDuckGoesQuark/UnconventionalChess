package chessagents.agents.gameagent.behaviours.meta;

import chessagents.GameContext;
import chessagents.agents.gameagent.GameAgentContext;
import chessagents.agents.gameagent.GameStatus;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder.Subscription;
import jade.util.Logger;


public class NotifySubscriberWhenGameReady extends SimpleBehaviour {

    private final Logger logger = Logger.getMyLogger(this.getClass().getName());
    private final Subscription subscription;
    private GameAgentContext context;
    private boolean finished = false;

    public NotifySubscriberWhenGameReady(Subscription subscription, GameAgentContext context) {
        this.subscription = subscription;
        this.context = context;
    }

    private boolean gameIsReady() {
        return context.getGameStatus() == GameStatus.READY;
    }

    @Override
    public void action() {
        if (gameIsReady()) {
            logger.info("Notifying subscriber: " + subscription.getMessage().getSender().getName());
            var subscriptionMessage = subscription.getMessage();
            var notification = subscriptionMessage.createReply();
            notification.setPerformative(ACLMessage.INFORM);
            notification.setContent(subscriptionMessage.getContent());
            subscription.notify(notification);
            finished = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return finished;
    }
}
