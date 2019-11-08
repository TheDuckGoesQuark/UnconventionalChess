package chessagents.agents.gatewayagent.messages;

import jade.content.ContentElement;

import java.util.Optional;

/**
 * Users of the chess gateway agent and its behaviours can provide custom message models to allow for seamless transition
 * between the ontological representations used by agents.
 *
 * @param <M> the message class being translated to
 */
public interface OntologyTranslator<M> {

    /**
     * Translate the given ontological element(s) to a custom message type
     *
     * @param ontology ontological element
     * @param type     message type to create
     * @return custom message type
     */
    Optional<M> toMessage(ContentElement ontology, MessageType type);

    /**
     * Translate the given message to an ontological representation
     *
     * @param message message to translate
     * @param type    message type being translated
     * @return ontological representation of message
     */
    Optional<ContentElement> toOntology(M message, MessageType type);

}
