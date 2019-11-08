package stacs.chessgateway.util;

import chessagents.agents.gatewayagent.messages.MessageType;
import chessagents.agents.gatewayagent.messages.MoveMessage;
import chessagents.agents.gatewayagent.messages.OntologyTranslator;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Move;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
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

    public MakeMove translateToOntology(MoveMessage move) {
        final MakeMove makeMove = new MakeMove();
        makeMove.setMove(new Move(move.getSourceSquare(), move.getTargetSquare()));
        return makeMove;
    }

    public Optional<MoveMessage> translateFromOntology(ACLMessage received) {
        try {
            final Action actionExpression = (Action) contentManager.extractContent(received);
            final Move move = ((MakeMove) actionExpression.getAction()).getMove();
            final MoveMessage moveMessage = new MoveMessage(move.getSource().getCoordinates(), move.getTarget().getCoordinates());

            return Optional.of(moveMessage);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Unable to translate from ontology: " + e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<MoveMessage> ontologyToMoveMessage(ContentElement message) {
        Optional<Move> result = Optional.empty();
        try {
            var absEquals = (AbsPredicate) myAgent.getContentManager().extractAbsContent(message);

            if (!absEquals.getTypeName().equals(BasicOntology.EQUALS)) {
                throw new NotUnderstoodException("Did not receive expected equals predicate");
            }

            var absRight = absEquals.getAbsTerm(BasicOntology.EQUALS_RIGHT);
            var move = (Move) ChessOntology.getInstance().toObject(absRight);
            result = Optional.of(move);
        } catch (Codec.CodecException | OntologyException | NotUnderstoodException e) {
            logger.warn("Failed to translate ontology to move message: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Message> toMessage(ContentElement ontology, MessageType type) {
        switch (type) {
            case MOVE_MESSAGE:
                return ontologyToMoveMessage(ontology).map(move -> new Message<MoveMessage>(Instant.now(), MessageType.MOVE_MESSAGE, move));
            case CHAT_MESSAGE:
                break;
            default:
                return Optional.empty();
        }
    }

    @Override
    public Optional<ContentElement> toOntology(Message message, MessageType type) {
        return Optional.empty();
    }
}
