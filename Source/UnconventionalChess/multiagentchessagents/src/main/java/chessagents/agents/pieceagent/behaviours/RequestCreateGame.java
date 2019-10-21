package chessagents.agents.pieceagent.behaviours;

import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.CreateGame;
import chessagents.ontology.schemas.concepts.Game;
import chessagents.ontology.schemas.predicates.IsReady;
import jade.content.Predicate;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import jade.util.Logger;

/**
 * Requests the game agent to create a chess game, and inform when it is done
 */
public class RequestCreateGame extends SimpleAchieveREInitiator {

    private static final Logger logger = Logger.getMyLogger(RequestCreateGame.class.getName());
    private final AID gameAgent;
    private final int gameId;

    public RequestCreateGame(Agent myAgent, AID gameAgent, int gameId) {
        super(myAgent, new ACLMessage(ACLMessage.REQUEST));
        this.gameAgent = gameAgent;
        this.gameId = gameId;
    }

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
            var createGame = new CreateGame(gameId);
            var action = new Action(gameAgent, createGame);
            myAgent.getContentManager().fillContent(request, action);
        } catch (Codec.CodecException | OntologyException e) {
            fail(e.getMessage());
        }
    }

    @Override
    protected void handleAgree(ACLMessage msg) {
        super.handleAgree(msg);
    }

    @Override
    protected void handleRefuse(ACLMessage msg) {
        super.handleRefuse(msg);
    }

    @Override
    protected void handleInform(ACLMessage msg) {
        try {
            var predicate = (AbsPredicate) myAgent.getContentManager().extractAbsContent(msg);

            if (predicate.getTypeName().equals(ChessOntology.IS_READY)) {
                logger.info("Game is ready.");
            }
        } catch (Codec.CodecException | OntologyException e) {
            fail(e.getMessage());
        }
    }

    private void fail(String reason) {
        logger.warning("Error: " + reason);
        myAgent.removeBehaviour(this);
        block();
    }
}
