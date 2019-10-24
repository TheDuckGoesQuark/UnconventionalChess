package chessagents.agents.gameagent.behaviours;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameStatus;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Piece;
import jade.content.abs.*;
import jade.content.lang.Codec;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import static chessagents.agents.gameagent.GameAgent.GAME_STATUS_KEY;
import static chessagents.agents.pieceagent.PieceAgent.AID_TO_PIECE_KEY;
import static chessagents.ontology.ChessOntology.*;

public class HandlePieceListRequests extends SimpleAchieveREResponder {
    public HandlePieceListRequests(GameAgent gameAgent, DataStore dataStore) {
        super(gameAgent, MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_QUERY),
                MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
        ), dataStore);
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) {
        // TODO this request doesnt seem to be being made?
        var reply = request.createReply();

        if (getDataStore().get(GAME_STATUS_KEY) != GameStatus.READY) {
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
            var abs = contentManager.extractContent(request);

            if (abs instanceof AbsIRE) {
                var ire = (AbsIRE) abs;
                var absIsColour = ire.getProposition();
                var ontology = contentManager.getOntology(request);
                var colour = (Colour) ontology.toObject(absIsColour.getAbsObject(IS_COLOUR_COLOUR));
                var matchingPieces = getPiecesForColour(colour.getColour());
                var answer = createAnswer(ire, matchingPieces, ontology);
                reply.setPerformative(ACLMessage.INFORM_REF);
                contentManager.fillContent(reply, answer);
            }
        } catch (Codec.CodecException | OntologyException e) {
            throw new FailureException("Unable to answer message: " + e.getMessage());
        }

        return reply;
    }

    private AbsPredicate createAnswer(AbsIRE ire, Set<Piece> matchingPieces, Ontology ontology) throws OntologyException {
        var aggregate = new AbsAggregate(BasicOntology.SET);

        for (Piece matchingPiece : matchingPieces) {
            aggregate.add((AbsTerm) ontology.fromObject(matchingPiece));
        }

        var equals = new AbsPredicate(BasicOntology.EQUALS);
        equals.set(BasicOntology.EQUALS_LEFT, ire);
        equals.set(BasicOntology.EQUALS_RIGHT, aggregate);
        return equals;
    }

    /**
     * Gets all pieces for the given colour
     *
     * @param colour
     * @return
     */
    private Set<Piece> getPiecesForColour(String colour) {
        return ((HashMap<AID, Piece>) getDataStore().get(AID_TO_PIECE_KEY)).values()
                .stream()
                .filter(p -> p.getColour().getColour().equals(colour))
                .collect(Collectors.toSet());
    }
}
