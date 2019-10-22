package chessagents.agents.pieceagent.behaviours;

import chessagents.ontology.schemas.concepts.Game;
import chessagents.ontology.schemas.predicates.IsReady;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import jade.util.Logger;

import java.util.Vector;

/**
 * Subscribes the given agent to the current game status
 */
public class SubscribeToGameStatus extends SubscriptionInitiator {

    private final Logger logger = Logger.getMyLogger(this.getClass().getName());
    private final Behaviour handleGameReady;
    private final AID gameAgent;
    private final Game game;

    public SubscribeToGameStatus(Agent a, Behaviour handleGameReady, AID gameAgent, Game game) {
        super(a, new ACLMessage(ACLMessage.SUBSCRIBE));

        this.handleGameReady = handleGameReady;
        this.gameAgent = gameAgent;
        this.game = game;
    }

    @Override
    protected Vector prepareSubscriptions(ACLMessage subscription) {
        subscription.addReceiver(gameAgent);

        try {
            myAgent.getContentManager().fillContent(subscription, new IsReady(game));
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Unable to create subscription message: " + e.getMessage());
        }

        return super.prepareSubscriptions(subscription);
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        super.handleAgree(agree);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        ContentElement content = null;
        try {
            content = myAgent.getContentManager().extractContent(inform);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Unable to extract contents of message: " + e.getMessage());
        }

        if (content instanceof IsReady) {
            myAgent.addBehaviour(handleGameReady);
            cancel(gameAgent, true);
            myAgent.removeBehaviour(this);
        }
    }
}
