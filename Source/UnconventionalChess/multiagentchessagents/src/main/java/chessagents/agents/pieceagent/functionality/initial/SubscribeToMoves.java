package chessagents.agents.pieceagent.functionality.initial;

import chessagents.agents.commonbehaviours.SubscriptionInitiator;
import chessagents.agents.pieceagent.PieceContext;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsVariable;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.UUID;

import static chessagents.agents.gameagent.behaviours.gameplay.HandleMoveSubscriptions.MOVE_SUBSCRIPTION_PROTOCOL;
import static chessagents.ontology.ChessOntology.MOVE_MADE;
import static chessagents.ontology.ChessOntology.MOVE_MADE_MOVE;

/**
 * Subscribes the given agent to moves being made in the game
 */
public class SubscribeToMoves extends SubscriptionInitiator {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext context;

    public SubscribeToMoves(PieceContext context) {
        super(MOVE_SUBSCRIPTION_PROTOCOL);
        this.context = context;
    }

    @Override
    public ACLMessage prepareSubscription(ACLMessage subscription) {
        subscription.addReceiver(context.getGameAgentAID());
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
    public void handleAgree(ACLMessage agree) {
        context.setMoveSubscriptionId(agree.getConversationId());
    }
}
