package chessagents.agents.commonbehaviours;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder;
import jade.util.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class for informing a set of subscribers of events as added to the queue
 */
public abstract class SubscriptionInform<E> extends CyclicBehaviour {

    protected final Logger logger = Logger.getMyLogger(getClass().getName());
    private final Set<SubscriptionResponder.Subscription> subs = new HashSet<>();
    private final Queue<E> events = new LinkedBlockingQueue<>();

    public void addSubscriber(SubscriptionResponder.Subscription sub) {
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
        var event = events.poll();

        if (event != null) {
            var stream = buildInformMessages(event);
            for (ACLMessage inform : stream) {
                myAgent.send(inform);
            }
        } else {
            block();
        }
    }

    private Collection<ACLMessage> buildInformMessages(E event) {
        var content = buildInformContents(event);
        return subs.stream().map(sub -> createSubscriptionInform(sub.getMessage(), content)).collect(Collectors.toUnmodifiableList());
    }

    private ACLMessage createSubscriptionInform(ACLMessage subscription, ContentElement content) {
        var reply = subscription.createReply();
        reply.setPerformative(ACLMessage.INFORM);

        try {
            myAgent.getContentManager().fillContent(reply, content);
            logger.info(reply.getContent());
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to serialise inform message: " + e.getMessage());
        }

        return reply;
    }

    /**
     * Add an event to the queue to inform consumers of
     *
     * @param event
     */
    public void addEvent(E event) {
        this.reset();
        events.add(event);
    }

    /**
     * Extending classes must provide the logic for constructing the contents of the subscription inform messages
     *
     * @return the contents of the subscription inform  messages
     */
    public abstract ContentElement buildInformContents(E event);

}
