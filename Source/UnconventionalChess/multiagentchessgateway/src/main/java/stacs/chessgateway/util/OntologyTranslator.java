package stacs.chessgateway.util;

import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Move;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import stacs.chessgateway.models.MoveMessage;

import java.util.Optional;

@Component
public class OntologyTranslator {

    private static final Logger logger = LoggerFactory.getLogger(OntologyTranslator.class);
    private final ContentManager contentManager;

    public OntologyTranslator() {
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
        // TODO too specific, what about chat messages??
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
}
