package chessagents.agents.pieceagent.behaviours;

import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Colour;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsPredicate;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

import static chessagents.agents.pieceagent.PieceAgent.GAME_AGENT_AID_KEY;
import static chessagents.agents.pieceagent.PieceAgent.MY_COLOUR_KEY;


public class RequestPieceIds extends SimpleAchieveREInitiator {
    public RequestPieceIds(Agent a, DataStore dataStore) {
        super(a, new ACLMessage(ACLMessage.QUERY_REF), dataStore);
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage request) {
        request.addReceiver((AID) getDataStore().get(GAME_AGENT_AID_KEY));
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setOntology(ChessOntology.ONTOLOGY_NAME);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);
        populateRequestContents(request);
        return request;
    }

    private void populateRequestContents(ACLMessage request) throws OntologyException {
        // query for all x where isColour(x, <mycolour>)
        var ontology = ChessOntology.getInstance();
        var colour = (Colour) getDataStore().get(MY_COLOUR_KEY);
        var absColour = (AbsConcept) ontology.fromObject(colour);
        absColour.set(ChessOntology.COLOUR_COLOUR, colour.getColour());
        var colourPredicate = new AbsPredicate(ChessOntology.IS_COLOUR);
        colourPredicate.set(ChessOntology.IS_COLOUR_COLOUR, colour);


    }


    @Override
    protected void handleAgree(ACLMessage agree) {
        super.handleAgree(agree);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        super.handleInform(inform);
    }
}
