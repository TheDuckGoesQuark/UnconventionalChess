package stacs.chessgateway.util;

import chessagents.agents.gatewayagent.messages.ChatMessage;
import chessagents.agents.gatewayagent.messages.MessageType;
import chessagents.agents.gatewayagent.messages.MoveMessage;
import chessagents.agents.gatewayagent.messages.OntologyTranslator;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Move;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.OntoAID;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsVariable;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import stacs.chessgateway.models.Message;

import java.time.Instant;
import java.util.Optional;

@Component
public class OntologyMessageTranslator implements OntologyTranslator<Message> {

    private static final Logger logger = LoggerFactory.getLogger(OntologyMessageTranslator.class);
    private final ContentManager contentManager;

    public OntologyMessageTranslator() {
        this.contentManager = new ContentManager();
        contentManager.registerOntology(ChessOntology.getInstance());
        contentManager.registerLanguage(new SLCodec());
    }

    private Optional<Object> ontologyToMoveMessage(ContentElement content) {
        Optional<Object> result = Optional.empty();

        try {
            var absEquals = (AbsPredicate) content;

            if (!absEquals.getTypeName().equals(BasicOntology.EQUALS)) {
                throw new NotUnderstoodException("Did not receive expected equals predicate");
            }

            var absRight = absEquals.getAbsTerm(BasicOntology.EQUALS_RIGHT);
            var move = (Move) ChessOntology.getInstance().toObject(absRight);

            var moveMessage = new MoveMessage();
            moveMessage.setSourceSquare(move.getSource().getCoordinates());
            moveMessage.setTargetSquare(move.getTarget().getCoordinates());
            // TODO add promotion to move concept

            result = Optional.of(moveMessage);
        } catch (OntologyException | NotUnderstoodException e) {
            logger.warn("Failed to translate ontology to move message: " + e.getMessage());
        }

        return result;
    }

    private Optional<Object> ontologyToChatMessage(ContentElement content) {
        Optional<Object> result = Optional.empty();

        try {
            var absEquals = (AbsPredicate) content;

            if (!absEquals.getTypeName().equals(BasicOntology.EQUALS)) {
                throw new NotUnderstoodException("Did not receive expected equals predicate");
            }

            // Right contains the values for the variables in the order they are specified in the left
            // that satisfy the predicate in the IRE
            var values = (AbsAggregate) absEquals.getAbsTerm(BasicOntology.EQUALS_RIGHT);
            var variables = ((AbsIRE) absEquals.getAbsTerm(BasicOntology.EQUALS_LEFT)).getVariables();

            var chatMessage = new ChatMessage();

            for (var i = 0; i < variables.getCount(); i++) {
                var variable = (AbsVariable) variables.get(i);

                switch (variable.getType()) {
                    case ChessOntology.SAID_TO_SPEAKER:
                        var speaker = (OntoAID) ChessOntology.getInstance().toObject(values.get(i));
                        chatMessage.setFromId(speaker.getLocalName());
                        break;
                    case ChessOntology.SAID_TO_LISTENER:
                        var listener = (OntoAID) ChessOntology.getInstance().toObject(values.get(i));
                        chatMessage.setToId(listener.getLocalName());
                        break;
                    case ChessOntology.SAID_TO_PHRASE:
                        var messageBody = (String) ChessOntology.getInstance().toObject(values.get(i));
                        chatMessage.setMessageBody(messageBody);
                        break;
                    default:
                        throw new NotUnderstoodException("Unable to parse variables of expression: " + content.toString());
                }
            }

            result = Optional.of(chatMessage);
        } catch (OntologyException | ClassCastException | NotUnderstoodException e) {
            logger.warn("Failed to translate ontology to move message: " + e.getMessage());
        }

        return result;

    }

    private Optional<ContentElement> createMoveOnto(Message message) {
        var move = (MoveMessage) message.getBody();

        final MakeMove makeMove = new MakeMove();
        makeMove.setMove(new Move(move.getSourceSquare(), move.getTargetSquare()));

        return Optional.of(makeMove);
    }

    private Optional<ContentElement> createChatOnto(Message message) {
        var chat = (ChatMessage) message.getBody();

        // TODO  construct chat onto

        return Optional.empty();
    }

    private Message wrapMessage(Object body, MessageType type) {
        return new Message<>(Instant.now(), type, body);
    }

    private Optional<Object> constructMessageBody(ContentElement contentElement, MessageType type) {
        Optional<Object> messageBody = Optional.empty();

        switch (type) {
            case MOVE_MESSAGE:
                messageBody = ontologyToMoveMessage(contentElement);
                break;
            case CHAT_MESSAGE:
                messageBody = ontologyToChatMessage(contentElement);
                break;
        }

        return messageBody;
    }

    @Override
    public Optional<Message> toMessage(ContentElement ontology, MessageType type) {
        return constructMessageBody(ontology, type)
                .map(body -> wrapMessage(body, type));
    }

    @Override
    public Optional<ContentElement> toOntology(Message message, MessageType type) {
        switch (type) {
            case CHAT_MESSAGE:
                return createChatOnto(message);
            case MOVE_MESSAGE:
                return createMoveOnto(message);
            default:
                return Optional.empty();
        }
    }

}
