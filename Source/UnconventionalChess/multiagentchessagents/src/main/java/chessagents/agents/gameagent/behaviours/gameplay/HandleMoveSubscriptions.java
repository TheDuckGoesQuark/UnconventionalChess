package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import chessagents.agents.gameagent.behaviours.meta.NotifySubscriberWhenGameReady;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.predicates.IsReady;
import chessagents.ontology.schemas.predicates.MoveMade;
import jade.content.abs.AbsIRE;
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

import static chessagents.ontology.ChessOntology.MOVE_MADE;

/**
 * Updates all subscribers each time a move is made
 */
public class HandleMoveSubscriptions extends SubscriptionResponder {

    private final Logger logger = Logger.getMyLogger(HandleMoveSubscriptions.class.getName());
    private final InformSubscribersOfMoves informSubscribersOfMoves;

    HandleMoveSubscriptions(GameAgent gameAgent, GameContext context) {
        super(gameAgent, MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE),
                MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
        ));
        this.informSubscribersOfMoves = new InformSubscribersOfMoves(context);
    }

    /**
     * Called whenever a subscription message is made
     *
     * @param subscription subscription message
     * @return agree to subscription message
     * @throws NotUnderstoodException if unable to understand subscription message
     */
    @Override
    protected ACLMessage handleSubscription(ACLMessage subscription) throws NotUnderstoodException {
        try {
            if (isMoveSubscription(subscription)) {
                var sub = createSubscription(subscription);
                informSubscribersOfMoves.addSubscriber(sub);
            }
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed: " + e.getMessage());
            throw new NotUnderstoodException(e.getMessage());
        }

        var agree = subscription.createReply();
        agree.setPerformative(ACLMessage.AGREE);

        return agree;
    }

    private boolean isMoveSubscription(ACLMessage subscription) throws Codec.CodecException, OntologyException {
        var abs = myAgent.getContentManager().extractAbsContent(subscription);

        var isMoveSubscription = false;
        var ontology = ChessOntology.getInstance();

        if (abs instanceof AbsIRE) {
            var ire = (AbsIRE) abs;
            var prop = ontology.fromObject(ire.getProposition());

            if (prop.getTypeName().equals(MOVE_MADE)) {
                isMoveSubscription = true;
            }
        }

        return isMoveSubscription;
    }

    @Override
    protected ACLMessage handleCancel(ACLMessage cancel) throws FailureException {
        informSubscribersOfMoves.removeSubscriber(cancel);
        return super.handleCancel(cancel);
    }
}
