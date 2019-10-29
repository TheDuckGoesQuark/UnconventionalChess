package chessagents.agents.pieceagent.behaviours;

import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Game;
import chessagents.ontology.schemas.predicates.IsReady;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

/**
 * Subscribes the given agent to the current game status
 */
public class SubscribeToMoves extends SimpleBehaviour {

    private static final MessageTemplate MESSAGE_TEMPLATE = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE),
            MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
    );
    private final Logger logger = Logger.getMyLogger(this.getClass().getName());
    private static final int PREPARE_SUBSCRIPTION = 0;
    private static final int SEND_SUBSCRIPTION_REQUEST = 1;
    private static final int WAIT_FOR_RESPONSE = 2;
    private static final int HANDLE_RESPONSE = 3;
    private static final int WAIT_FOR_INFORM = 4;
    private static final int HANDLE_INFORM = 5;
    private static final int CANCEL_SUBSCRIPTION = 6;
    private static final int DONE = 7;

    private static final String REQUEST_KEY = "_REQUEST";
    private static final String RESPONSE_KEY = "_RESPONSE";
    private static final String RESULT_KEY = "_RESULT";
    private final AID gameAgentAID;
    private final Game game;

    private int state = PREPARE_SUBSCRIPTION;

    public SubscribeToMoves(Agent a, AID gameAgentAID, Game game) {
        super(a);
        this.gameAgentAID = gameAgentAID;
        this.game = game;
    }

    private ACLMessage prepareSubscription(ACLMessage subscription) {
        subscription.addReceiver(gameAgentAID);
        subscription.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        subscription.setOntology(ChessOntology.ONTOLOGY_NAME);
        subscription.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);

        try {
            myAgent.getContentManager().fillContent(subscription, new IsReady(game));
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
                logger.info("Preparing subscription");
                request = this.prepareSubscription(new ACLMessage(ACLMessage.SUBSCRIBE));
                getDataStore().put(REQUEST_KEY, request);
                state = SEND_SUBSCRIPTION_REQUEST;
                break;
            case SEND_SUBSCRIPTION_REQUEST:
                logger.info("Sending subscription");
                request = (ACLMessage) getDataStore().get(REQUEST_KEY);
                myAgent.send(request);
                state = WAIT_FOR_RESPONSE;
                break;
            case WAIT_FOR_RESPONSE:
                if (receiveMessage(RESPONSE_KEY)) {
                    logger.info("Received subscription response");
                    state = HANDLE_RESPONSE;
                } else {
                    block();
                }
                break;
            case HANDLE_RESPONSE:
                response = (ACLMessage) getDataStore().get(RESPONSE_KEY);
                if (response.getPerformative() == ACLMessage.AGREE) {
                    logger.info("Subscription AGREE received");
                    state = WAIT_FOR_INFORM;
                } else {
                    state = PREPARE_SUBSCRIPTION;
                }
                break;
            case WAIT_FOR_INFORM:
                if (receiveMessage(RESULT_KEY)) {
                    logger.info("Game status INFORM received");
                    state = HANDLE_INFORM;
                } else {
                    block();
                }
                break;
            case HANDLE_INFORM:
                response = (ACLMessage) getDataStore().get(RESULT_KEY);
                if (response.getPerformative() == ACLMessage.INFORM) {
                    state = CANCEL_SUBSCRIPTION;
                } else {
                    state = PREPARE_SUBSCRIPTION;
                }
                break;
            case CANCEL_SUBSCRIPTION:
                logger.info("Canceling subscription");
                response = (ACLMessage) getDataStore().get(RESULT_KEY);
                cancelSubscription(response.createReply());
                state = DONE;
                break;
        }
    }

    private void cancelSubscription(ACLMessage cancel) {
        cancel.setPerformative(ACLMessage.CANCEL);
        myAgent.send(cancel);
    }

    private boolean receiveMessage(String resultKey) {
        var response = myAgent.receive(MESSAGE_TEMPLATE);
        var responseReceieved = response != null;

        if (responseReceieved) {
            getDataStore().put(resultKey, response);
        }

        return responseReceieved;
    }

    @Override
    public boolean done() {
        return this.state == DONE;
    }
}
