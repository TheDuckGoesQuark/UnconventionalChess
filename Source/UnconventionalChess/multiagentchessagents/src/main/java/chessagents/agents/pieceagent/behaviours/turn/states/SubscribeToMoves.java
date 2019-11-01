package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.ChessOntology;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsVariable;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import java.util.UUID;

import static chessagents.agents.gameagent.behaviours.gameplay.HandleMoveSubscriptions.MOVE_SUBSCRIPTION_PROTOCOL;
import static chessagents.agents.pieceagent.behaviours.turn.states.SubscribeToMoves.SubscriptionState.*;
import static chessagents.ontology.ChessOntology.*;

/**
 * Subscribes the given agent to moves being made in the game
 */
public class SubscribeToMoves extends SimpleBehaviour {

    enum SubscriptionState {
        PREPARE_SUBSCRIPTION,
        SEND_SUBSCRIPTION_REQUEST,
        WAIT_FOR_RESPONSE,
        HANDLE_RESPONSE,
        SUBSCRIBED,
    }

    private static final MessageTemplate MESSAGE_TEMPLATE = MessageTemplate.and(
            MessageTemplate.MatchProtocol(MOVE_SUBSCRIPTION_PROTOCOL),
            MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
    );

    private static final String REQUEST_KEY = "_REQUEST";
    private static final String RESPONSE_KEY = "_RESPONSE";
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext context;

    private SubscriptionState state = PREPARE_SUBSCRIPTION;

    public SubscribeToMoves(Agent agent, PieceContext context) {
        super(agent);
        this.context = context;
    }

    private ACLMessage prepareSubscription(ACLMessage subscription) {
        subscription.addReceiver(context.getGameAgentAID());
        subscription.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        subscription.setOntology(ChessOntology.ONTOLOGY_NAME);
        subscription.setProtocol(MOVE_SUBSCRIPTION_PROTOCOL);
        subscription.setConversationId(UUID.randomUUID().toString());

        var absVariableMove = new AbsVariable("move", MOVE_MADE_MOVE);
        var absMoveMade = new AbsPredicate(MOVE_MADE);
        absMoveMade.set(MOVE_MADE_MOVE, absVariableMove);

        var ire = new AbsIRE(SLVocabulary.IOTA);
        ire.setProposition(absMoveMade);
        ire.setVariable(absVariableMove);

        try {
            myAgent.getContentManager().fillContent(subscription, ire);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Unable to create move subscription message: " + e.getMessage());
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
                    context.setMoveSubscriptionId(response.getConversationId());
                    state = SUBSCRIBED;
                } else {
                    logger.warning("Received non-agree to subscription:" + response.getPerformative());
                    state = PREPARE_SUBSCRIPTION;
                }
                break;
        }
    }

    private boolean receiveResponse() {
        var response = myAgent.receive(MESSAGE_TEMPLATE);
        var responseReceived = response != null;

        if (responseReceived) {
            getDataStore().put(SubscribeToMoves.RESPONSE_KEY, response);
        }

        return responseReceived;
    }

    @Override
    public boolean done() {
        return this.state == SUBSCRIBED;
    }
}
