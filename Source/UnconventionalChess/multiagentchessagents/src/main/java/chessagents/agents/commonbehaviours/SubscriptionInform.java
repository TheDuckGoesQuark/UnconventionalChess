package chessagents.agents.commonbehaviours;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder;
import jade.util.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Class for informing a set of subscribers of some given event
 */
public abstract class SubscriptionInform extends OneShotBehaviour {
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final Set<SubscriptionResponder.Subscription> subs = new HashSet<>();

    public void addSubscriber(SubscriptionResponder.Subscription sub) {
        logger.info("New subscriber to moves: " + sub.getMessage().getSender());
        subs.add(sub);
    }

    public void removeSubscriber(String conversationId) {
        subs.stream()
                .filter(s -> s.getMessage().getConversationId().equals(conversationId))
                .findFirst()
                .ifPresent(subs::remove);
    }

    @Override
    public final void action() {
        buildInformMessages().forEach(inform -> myAgent.send(inform));
    }

    private Stream<ACLMessage> buildInformMessages() {
        var content = buildInformContents();
        return subs.stream().map(sub -> createSubscriptionInform(sub.getMessage(), content));
    }

    private ACLMessage createSubscriptionInform(ACLMessage subscription, ContentElement content) {
        var reply = subscription.createReply();
        reply.setPerformative(ACLMessage.INFORM_REF);

        try {
            myAgent.getContentManager().fillContent(reply, content);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to serialise inform message: " + e.getMessage());
        }

        return reply;
    }

    public abstract ContentElement buildInformContents();
}
