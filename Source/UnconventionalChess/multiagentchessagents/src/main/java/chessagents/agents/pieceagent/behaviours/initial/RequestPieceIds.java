package chessagents.agents.pieceagent.behaviours.initial;

import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.content.abs.*;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import jade.util.Logger;

import java.util.HashSet;
import java.util.Set;

import static chessagents.agents.gameagent.behaviours.meta.HandlePieceListRequests.PIECE_LIST_QUERY_PROTOCOL;


public class RequestPieceIds extends SimpleAchieveREInitiator {

    private final Logger logger = Logger.getMyLogger(this.getClass().getName());
    private final PieceContext context;

    public RequestPieceIds(PieceAgent pieceAgent, PieceContext context) {
        super(pieceAgent, ChessMessageBuilder.constructMessage(ACLMessage.QUERY_REF));
        this.context = context;
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage request) {
        request.addReceiver(context.getGameAgentAID());
        request.setProtocol(PIECE_LIST_QUERY_PROTOCOL);

        try {
            populateRequestContents(request);
        } catch (OntologyException | Codec.CodecException e) {
            logger.warning("Failed to serialise query: " + e.getMessage());
            request = null;
        }

        return request;
    }

    private void populateRequestContents(ACLMessage request) throws OntologyException, Codec.CodecException {
        // query for all x where isColour(x, <mycolour>)
        var ontology = ChessOntology.getInstance();

        // create abstract descriptor for colour
        var absColour = (AbsConcept) ontology.fromObject(context.getMyPiece().getColour());

        // declare variable in our query to be of type piece
        var absX = new AbsVariable("x", ChessOntology.PIECE);

        // query for all pieces with my colour (i.e. piece is variable)
        var absIsColour = new AbsPredicate(ChessOntology.IS_COLOUR);
        absIsColour.set(ChessOntology.IS_COLOUR_PIECE, absX);
        absIsColour.set(ChessOntology.IS_COLOUR_COLOUR, absColour);

        // Match ALL for which predicate is true
        var absIRE = new AbsIRE(SLVocabulary.ALL);
        absIRE.setVariable(absX);
        absIRE.setProposition(absIsColour);

        myAgent.getContentManager().fillContent(request, absIRE);
    }

    @Override
    protected void handleRefuse(ACLMessage msg) {
        logger.warning("Game Agent didn't like our request :( : " + msg.getContent());
        super.handleRefuse(msg);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            var contentManager = myAgent.getContentManager();
            var absEquals = (AbsPredicate) contentManager.extractAbsContent(inform);
            var absSet = (AbsAggregate) absEquals.getAbsTerm(BasicOntology.EQUALS_RIGHT);
            var pieces = extractPieces(absSet, contentManager.getOntology(inform));
            context.getGameState().registerPiecesAsAgents(pieces);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to extract message: " + e.getMessage());
        }
    }

    private Set<ChessPiece> extractPieces(AbsAggregate absSet, Ontology ontology) throws OntologyException {
        var pieces = new HashSet<ChessPiece>(absSet.getCount());

        for (int i = 0; i < absSet.getCount(); i++) {
            pieces.add((ChessPiece) ontology.toObject(absSet.get(i)));
        }

        return pieces;
    }
}
