package chessagents.agents.pieceagent.behaviours;

import chessagents.agents.gameagent.GameAgent;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Game;
import chessagents.ontology.schemas.predicates.IsReady;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import jade.util.Logger;

import java.util.Vector;

import static chessagents.agents.pieceagent.PieceAgent.GAME_AGENT_AID_KEY;
import static chessagents.agents.pieceagent.PieceAgent.GAME_KEY;

/**
 * Subscribes the given agent to the current game status
 */
public class SubscribeToGameStatus extends SubscriptionInitiator {

    private final Logger logger = Logger.getMyLogger(this.getClass().getName());
    private final Behaviour handleGameReady;

    public SubscribeToGameStatus(Agent a, Behaviour handleGameReady, DataStore dataStore) {
        super(a, new ACLMessage(ACLMessage.SUBSCRIBE), dataStore);
        this.handleGameReady = handleGameReady;
    }

    private AID getGameAgentAID() {
        return (AID) getDataStore().get(GAME_AGENT_AID_KEY);
    }


    @Override
    protected Vector prepareSubscriptions(ACLMessage subscription) {
        subscription.addReceiver(getGameAgentAID());
        subscription.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        subscription.setOntology(ChessOntology.ONTOLOGY_NAME);
        subscription.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);

        try {
            myAgent.getContentManager().fillContent(subscription, new IsReady((Game) getDataStore().get(GAME_KEY)));
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
            logger.info("Game Ready notification received");
            cancel(getGameAgentAID(), true);
            myAgent.removeBehaviour(this);
        }
    }
}
