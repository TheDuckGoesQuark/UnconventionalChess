package chessagents.agents.gameagent.behaviours.meta;

import chessagents.agents.gameagent.GameAgent;
import chessagents.GameContext;
import chessagents.agents.gameagent.GameAgentContext;
import chessagents.agents.gameagent.GameStatus;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Piece;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;
import jade.util.Logger;

import java.util.Set;
import java.util.stream.Collectors;

import static chessagents.ontology.ChessOntology.IS_COLOUR_COLOUR;

public class HandlePieceListRequests extends SimpleAchieveREResponder {

    public static final String PIECE_LIST_QUERY_PROTOCOL = "PieceListQueryProtocol";
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final GameAgentContext context;

    public HandlePieceListRequests(GameAgent gameAgent, GameAgentContext context) {
        super(gameAgent, MessageTemplate.and(
                MessageTemplate.MatchProtocol(PIECE_LIST_QUERY_PROTOCOL),
                MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
        ));
        this.context = context;
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) {
        var reply = request.createReply();

        if (context.getGameStatus() != GameStatus.READY) {
            reply.setPerformative(ACLMessage.REFUSE);
        } else {
            reply.setPerformative(ACLMessage.AGREE);
        }

        return reply;
    }

    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
        var reply = request.createReply();

        try {
            var contentManager = myAgent.getContentManager();
            var abs = contentManager.extractAbsContent(request);
            var ontology = ChessOntology.getInstance();

            if (abs instanceof AbsIRE) {
                var ire = (AbsIRE) abs;
                var prop = ontology.fromObject(ire.getProposition());
                var colour = (Colour) ontology.toObject(prop.getAbsObject(IS_COLOUR_COLOUR));
                var matchingPieces = context.getGameContext().getPiecesForColour(colour);
                var answer = createAnswer(ire, matchingPieces, ontology);
                reply.setPerformative(ACLMessage.INFORM);
                contentManager.fillContent(reply, answer);
            }
        } catch (Codec.CodecException | OntologyException | NullPointerException e) {
            logger.warning("bad thing: " + e.getMessage());
            throw new FailureException("Unable to answer message: " + e.getMessage());
        }

        return reply;
    }

    private AbsPredicate createAnswer(AbsIRE ire, Set<Piece> matchingPieces, Ontology ontology) throws OntologyException {
        var aggregate = new AbsAggregate(BasicOntology.SET);

        for (Piece matchingPiece : matchingPieces) {
            aggregate.add((AbsConcept) ontology.fromObject(matchingPiece));
        }

        var equals = new AbsPredicate(BasicOntology.EQUALS);
        equals.set(BasicOntology.EQUALS_LEFT, ire);
        equals.set(BasicOntology.EQUALS_RIGHT, aggregate);
        return equals;
    }
}
