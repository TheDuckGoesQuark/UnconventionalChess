package chessagents.agents.pieceagent.behaviours;

import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Game;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import jade.util.Logger;

import java.util.Set;

public class PopulatePieceAgentList extends SimpleAchieveREInitiator {

    private static final Logger logger = Logger.getMyLogger(PopulatePieceAgentList.class.getName());

    private final Set<AID> pieceAgents;
    private final Colour myColour;
    private final AID gameAgent;

    @Override
    protected ACLMessage prepareRequest(ACLMessage request) {
        request.addReceiver(gameAgent);
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setOntology(ChessOntology.ONTOLOGY_NAME);
        populateContents(request);
        return request;
    }

    private void populateContents(ACLMessage request) {
        try {
            var isReady = new IsReady();
            isReady.setGame(new Game());
            var predicate = new AbsPredicate(ChessOntology.IS_READY);
            myAgent.getContentManager().fillContent(request, predicate);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Error when filling contents of agent list request: " + e.getMessage());
            myAgent.removeBehaviour(this);
            block();
        }
    }

    public PopulatePieceAgentList(Agent myAgent, Set<AID> pieceAgents, Colour myColour, AID gameAgent) {
        super(myAgent, new ACLMessage(ACLMessage.QUERY_IF));
        this.pieceAgents = pieceAgents;
        this.myColour = myColour;
        this.gameAgent = gameAgent;
    }

    @Override
    protected void handleInform(ACLMessage msg) {
        super.handleInform(msg);
    }
}
