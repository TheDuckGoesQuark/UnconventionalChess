package chessagents.agents.gameagent.behaviours.meta;

import chessagents.agents.gameagent.GameAgent;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.predicates.IsReady;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.util.Logger;

public class HandleGameStatusSubscriptions extends SubscriptionResponder {

    private final Logger logger = Logger.getMyLogger(HandleGameStatusSubscriptions.class.getName());

    public HandleGameStatusSubscriptions(GameAgent gameAgent, DataStore datastore) {
        super(gameAgent, MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE),
                MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
        ), null, datastore);
    }

    /**
     * Called whenever a subscription message is made
     *
     * @param subscription subscription message
     * @return
     * @throws NotUnderstoodException
     */
    @Override
    protected ACLMessage handleSubscription(ACLMessage subscription) throws NotUnderstoodException {
        try {
            var content = myAgent.getContentManager().extractContent(subscription);

            if (content instanceof IsReady) {
                var sub = createSubscription(subscription);
                myAgent.addBehaviour(new NotifySubscriberWhenGameReady(sub, getDataStore()));
            }
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed: " + e.getMessage());
            throw new NotUnderstoodException(e.getMessage());
        }

        var agree = subscription.createReply();
        agree.setPerformative(ACLMessage.AGREE);

        return agree;
    }

    @Override
    protected ACLMessage handleCancel(ACLMessage cancel) throws FailureException {
        return super.handleCancel(cancel);
    }
}
