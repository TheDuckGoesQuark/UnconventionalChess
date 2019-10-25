package chessagents.agents.gameagent.behaviours;

import chessagents.agents.gameagent.GameStatus;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder.Subscription;
import jade.util.Logger;


import static chessagents.agents.gameagent.GameAgent.GAME_STATUS_KEY;
import static chessagents.ontology.ChessOntology.IS_READY;

public class NotifySubscriberWhenGameReady extends SimpleBehaviour {

    private final Logger logger = Logger.getMyLogger(this.getClass().getName());
    private final Subscription subscription;
    private boolean finished = false;

    public NotifySubscriberWhenGameReady(Subscription subscription, DataStore dataStore) {
        this.subscription = subscription;
        this.setDataStore(dataStore);
    }

    private boolean gameIsReady() {
        return getDataStore().get(GAME_STATUS_KEY) == GameStatus.READY;
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
