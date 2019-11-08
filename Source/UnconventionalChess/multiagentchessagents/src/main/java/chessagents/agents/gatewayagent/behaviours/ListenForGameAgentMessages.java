package chessagents.agents.gatewayagent.behaviours;

import chessagents.agents.gatewayagent.MessageHandler;
import chessagents.agents.gatewayagent.messages.MessageType;
import chessagents.agents.gatewayagent.messages.OntologyTranslator;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Move;
import jade.content.ContentElement;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static chessagents.agents.gameagent.behaviours.gameplay.HandleMoveSubscriptions.MOVE_SUBSCRIPTION_PROTOCOL;

/**
 * Cyclic class for listening to any incoming messages from the gateway agent that accepts a message handler implementation
 * as a parameter
 *
 * @param <M> base message class for translated messages
 */
public class ListenForGameAgentMessages<M> extends CyclicBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final OntologyTranslator<M> ontologyTranslator;
    private final MessageHandler<M> messageHandler;
    private final MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchProtocol(MOVE_SUBSCRIPTION_PROTOCOL),
            MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF)
    );

    public ListenForGameAgentMessages(OntologyTranslator<M> ontologyTranslator, MessageHandler<M> messageHandler) {
        this.ontologyTranslator = ontologyTranslator;
        this.messageHandler = messageHandler;
    }

    @Override
    public void action() {
        var agentMessage = myAgent.receive(mt);

        if (agentMessage != null) {
            extractContent(agentMessage)
                    .flatMap(contentElement -> determineMessageType(agentMessage)
                            .flatMap(messageType -> ontologyTranslator.toMessage(contentElement, messageType)))
                    .ifPresent(message -> messageHandler.handleAgentMessage(message, agentMessage.getSender()));
        } else {
            block();
        }
    }

    /**
     * Determines which kind of message has been received by the gateway agent
     *
     * @param agentMessage message that was received
     * @return type of message
     */
    private Optional<MessageType> determineMessageType(ACLMessage agentMessage) {
        // TODO add chat protocol
        switch (agentMessage.getConversationId()) {
            case MOVE_SUBSCRIPTION_PROTOCOL:
                return Optional.of(MessageType.MOVE_MESSAGE);
            default:
                logger.warning("Unknown message type or protocol: " + agentMessage.getConversationId());
                return Optional.empty();
        }
    }

    /**
     * Extracts the content of the message. If no variables exist in the ontological statement then
     * the absolute ontology instances will be extracted, otherwise they will remain as abstract.
     *
     * @param message message to extract content from
     * @return the message content if able to extract it
     */
    private Optional<ContentElement> extractContent(ACLMessage message) {
        Optional<ContentElement> result = Optional.empty();

        try {
            var contentManager = myAgent.getContentManager();
            var abs = contentManager.extractAbsContent(message);

            // if no variables exist in the content
            if (abs.isGrounded()) {
                result = Optional.ofNullable(contentManager.extractContent(message));
            } else {
                result = Optional.of(abs);
            }
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to extract agent message: " + e.getMessage());
        }

        return result;
    }
}
