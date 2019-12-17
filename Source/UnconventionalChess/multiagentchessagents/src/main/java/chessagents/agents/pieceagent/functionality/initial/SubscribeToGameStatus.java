package chessagents.agents.pieceagent.functionality.initial;

import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Game;
import chessagents.ontology.schemas.predicates.IsReady;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import java.util.UUID;

import static chessagents.agents.gameagent.behaviours.meta.HandleGameStatusSubscriptions.GAME_STATUS_SUBSCRIPTION_PROTOCOL;

/**
 * Subscribes the given agent to the current game status
 */
public class SubscribeToGameStatus extends SimpleBehaviour {

    enum SubscriptionState {
        PREPARE_SUBSCRIPTION,
        SEND_SUBSCRIPTION_REQUEST,
        WAIT_FOR_RESPONSE,
        HANDLE_RESPONSE,
        WAIT_FOR_INFORM,
        HANDLE_INFORM,
        CANCEL_SUBSCRIPTION,
        DONE,
    }

    private static final MessageTemplate MESSAGE_TEMPLATE = MessageTemplate.and(
            MessageTemplate.MatchProtocol(GAME_STATUS_SUBSCRIPTION_PROTOCOL),
            MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
    );
    private static final String REQUEST_KEY = "_REQUEST";
    private static final String RESPONSE_KEY = "_RESPONSE";
    private static final String RESULT_KEY = "_RESULT";

    private final Logger logger = Logger.getMyLogger(this.getClass().getName());
    private final PieceContext context;
    private SubscriptionState state = SubscriptionState.PREPARE_SUBSCRIPTION;

    public SubscribeToGameStatus(PieceAgent pieceAgent, PieceContext context) {
        super(pieceAgent);
        this.context = context;
    }

    private ACLMessage prepareSubscription(ACLMessage subscription) {
        subscription.addReceiver(context.getGameAgentAID());
        subscription.setProtocol(GAME_STATUS_SUBSCRIPTION_PROTOCOL);
        subscription.setConversationId(UUID.randomUUID().toString());

        var content = new IsReady(new Game(context.getGameState().getGameId()));
        try {
            myAgent.getContentManager().fillContent(subscription, content);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Unable to create subscription message: " + e.getMessage());
        }

        return subscription;
    }

    @Override
    public void action() {
        ACLMessage request, response;

        switch (this.state) {
            case PREPARE_SUBSCRIPTION:
                request = this.prepareSubscription(ChessMessageBuilder.constructMessage(ACLMessage.SUBSCRIBE));
                getDataStore().put(REQUEST_KEY, request);
                state = SubscriptionState.SEND_SUBSCRIPTION_REQUEST;
                break;
            case SEND_SUBSCRIPTION_REQUEST:
                request = (ACLMessage) getDataStore().get(REQUEST_KEY);
                myAgent.send(request);
                state = SubscriptionState.WAIT_FOR_RESPONSE;
                break;
            case WAIT_FOR_RESPONSE:
                if (receiveMessage(RESPONSE_KEY)) {
                    state = SubscriptionState.HANDLE_RESPONSE;
                } else {
                    block();
                }
                break;
            case HANDLE_RESPONSE:
                response = (ACLMessage) getDataStore().get(RESPONSE_KEY);
                if (response.getPerformative() == ACLMessage.AGREE) {
                    state = SubscriptionState.WAIT_FOR_INFORM;
                } else {
                    state = SubscriptionState.PREPARE_SUBSCRIPTION;
                }
                break;
            case WAIT_FOR_INFORM:
                if (receiveMessage(RESULT_KEY)) {
                    state = SubscriptionState.HANDLE_INFORM;
                } else {
                    block();
                }
                break;
            case HANDLE_INFORM:
                response = (ACLMessage) getDataStore().get(RESULT_KEY);
                if (response.getPerformative() == ACLMessage.INFORM) {
                    state = SubscriptionState.CANCEL_SUBSCRIPTION;
                } else {
                    state = SubscriptionState.PREPARE_SUBSCRIPTION;
                }
                break;
            case CANCEL_SUBSCRIPTION:
                response = (ACLMessage) getDataStore().get(RESULT_KEY);
                cancelSubscription(response.createReply());
                state = SubscriptionState.DONE;
                break;
        }
    }

    private void cancelSubscription(ACLMessage cancel) {
        cancel.setPerformative(ACLMessage.CANCEL);
        myAgent.send(cancel);
    }

    private boolean receiveMessage(String resultKey) {
        var response = myAgent.receive(MESSAGE_TEMPLATE);
        var responseReceived = response != null;

        if (responseReceived) {
            getDataStore().put(resultKey, response);
        }

        return responseReceived;
    }

    @Override
    public boolean done() {
        return this.state == SubscriptionState.DONE;
    }
}
