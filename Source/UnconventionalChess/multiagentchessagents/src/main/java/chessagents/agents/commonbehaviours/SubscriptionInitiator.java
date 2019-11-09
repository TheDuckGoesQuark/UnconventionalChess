package chessagents.agents.commonbehaviours;

import chessagents.agents.ChessMessageBuilder;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;


/**
 * Custom subscription initiator implementation that allows for
 * inform messages to be handled in a separate behaviour rather than
 * as part of this behaviour
 */
public abstract class SubscriptionInitiator extends SimpleBehaviour {

    enum SubscriptionState {
        PREPARE_SUBSCRIPTION,
        SEND_SUBSCRIPTION_REQUEST,
        WAIT_FOR_RESPONSE,
        HANDLE_RESPONSE,
        SUBSCRIBED,
    }

    private static final String REQUEST_KEY = "_REQUEST";
    private static final String RESPONSE_KEY = "_RESPONSE";
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private String protocolName;

    private SubscriptionState state = SubscriptionState.PREPARE_SUBSCRIPTION;
    private MessageTemplate messageTemplate;

    public SubscriptionInitiator(String protocolName) {
        this.protocolName = protocolName;
    }

    public abstract ACLMessage prepareSubscription(ACLMessage subscription);

    public abstract void handleAgree(ACLMessage agree);

    @Override
    public final void action() {
        ACLMessage request, response;

        switch (this.state) {
            case PREPARE_SUBSCRIPTION:
                logger.info("Preparing subscription");
                request = this.prepareSubscription(ChessMessageBuilder.constructMessage(ACLMessage.SUBSCRIBE));
                getDataStore().put(REQUEST_KEY, request);
                messageTemplate = MessageTemplate.and(
                        MessageTemplate.MatchProtocol(protocolName),
                        MessageTemplate.MatchConversationId(request.getConversationId())
                );
                state = SubscriptionState.SEND_SUBSCRIPTION_REQUEST;
                break;
            case SEND_SUBSCRIPTION_REQUEST:
                logger.info("Sending subscription");
                request = (ACLMessage) getDataStore().get(REQUEST_KEY);
                myAgent.send(request);
                state = SubscriptionState.WAIT_FOR_RESPONSE;
                break;
            case WAIT_FOR_RESPONSE:
                if (receiveResponse()) {
                    logger.info("Received subscription response");
                    state = SubscriptionState.HANDLE_RESPONSE;
                } else {
                    block();
                }
                break;
            case HANDLE_RESPONSE:
                response = (ACLMessage) getDataStore().get(RESPONSE_KEY);
                if (response.getPerformative() == ACLMessage.AGREE) {
                    logger.info("Subscription AGREE received");
                    this.handleAgree(response);
                    state = SubscriptionState.SUBSCRIBED;
                } else {
                    logger.warning("Received non-agree to subscription:" + response.getPerformative());
                    state = SubscriptionState.PREPARE_SUBSCRIPTION;
                }
                break;
        }
    }

    private boolean receiveResponse() {
        var response = myAgent.receive(messageTemplate);
        var responseReceived = response != null;

        if (responseReceived) {
            getDataStore().put(RESPONSE_KEY, response);
        }

        return responseReceived;
    }

    @Override
    public final boolean done() {
        return this.state == SubscriptionState.SUBSCRIBED;
    }
}
