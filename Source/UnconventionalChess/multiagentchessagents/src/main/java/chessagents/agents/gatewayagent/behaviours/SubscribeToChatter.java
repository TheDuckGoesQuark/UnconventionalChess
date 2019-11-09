package chessagents.agents.gatewayagent.behaviours;

import chessagents.agents.commonbehaviours.SubscriptionInitiator;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.gameagent.behaviours.chat.HandleChat.CHAT_PROTOCOL;

/**
 * Subscribes the given agent to the human-readable chatter between agents
 */
public class SubscribeToChatter extends SubscriptionInitiator {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final AID chatterBroker;

    public SubscribeToChatter(AID chatterBroker) {
        super(CHAT_PROTOCOL);
        this.chatterBroker = chatterBroker;
    }

    @Override
    public ACLMessage prepareSubscription(ACLMessage subscription) {
        subscription.addReceiver(chatterBroker);
        subscription.setProtocol(CHAT_PROTOCOL);
        return subscription;
    }

    @Override
    public void handleAgree(ACLMessage agree) {
        logger.info("Subscribed to chatter.");
    }
}
