package chessagents.agents.pieceagent.behaviours;

import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Piece;
import jade.content.abs.*;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import jade.util.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static chessagents.agents.gameagent.behaviours.meta.HandlePieceListRequests.PIECE_LIST_QUERY_PROTOCOL;


public class RequestPieceIds extends SimpleAchieveREInitiator {

    private final Logger logger = Logger.getMyLogger(this.getClass().getName());
    private final Map<AID, Piece> aidToPiece;
    private final Colour myColour;
    private final AID gameAgentAID;

    public RequestPieceIds(Agent a, Map<AID, Piece> aidToPiece, Colour myColour, AID gameAgentAID) {
        super(a, new ACLMessage(ACLMessage.QUERY_REF));
        this.aidToPiece = aidToPiece;
        this.myColour = myColour;
        this.gameAgentAID = gameAgentAID;
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage request) {
        request.addReceiver(gameAgentAID);
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setOntology(ChessOntology.ONTOLOGY_NAME);
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
        var absColour = (AbsConcept) ontology.fromObject(myColour);

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
    protected void handleAgree(ACLMessage agree) {
        logger.info("GameAgent agreed to give list of pieces! GET EXCITED");
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            var contentManager = myAgent.getContentManager();
            var absEquals = (AbsPredicate) contentManager.extractAbsContent(inform);
            var absSet = (AbsAggregate) absEquals.getAbsTerm(BasicOntology.EQUALS_RIGHT);
            var pieces = extractPieces(absSet, contentManager.getOntology(inform));
            pieces.forEach(piece -> aidToPiece.put(piece.getAgentAID(), piece));
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to extract message: " + e.getMessage());
        }
    }

    private Set<Piece> extractPieces(AbsAggregate absSet, Ontology ontology) throws OntologyException {
        var pieces = new HashSet<Piece>(absSet.getCount());

        for (int i = 0; i < absSet.getCount(); i++) {
            pieces.add((Piece) ontology.toObject(absSet.get(i)));
        }

        return pieces;
    }
}
