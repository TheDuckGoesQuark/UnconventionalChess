package chessagents.agents.commonbehaviours;

import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.MakeMove;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import static jade.content.lang.Codec.CodecException;

public class RequestGameAgentMove extends SimpleBehaviour {

    private static final MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
            MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
    );

    private static final int SENDING_REQUEST = 1;
    private static final int RECEIVE_RESPONSE = 2;
    private static final int RECEIVE_RESULT = 3;
    private static final int DONE = 4;

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final AID gameAgentName;
    private final MakeMove move;

    private int state = SENDING_REQUEST;
    private boolean moveMade = false;

    public RequestGameAgentMove(MakeMove move, AID gameAgentName) {
        this.move = move;
        this.gameAgentName = gameAgentName;
    }

    private ACLMessage prepareRequest(ACLMessage request) {
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setOntology(ChessOntology.ONTOLOGY_NAME);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        try {
            var makeMove = new Action(gameAgentName, move);
            myAgent.getContentManager().fillContent(request, makeMove);
        } catch (CodecException | OntologyException e) {
            logger.warning("Failed to serialise move request to SL: " + e.getMessage());
        }

        return request;
    }

    private void handleAgree(ACLMessage agree) {
        logger.info("Game agent agreed to make move!");
    }

    private void handleRefuse(ACLMessage refuse) {
        logger.warning("Game agent refused to make move! Reason: " + refuse.getContent());
    }

    private void handleNotUnderstood(ACLMessage notUnderstood) {
        logger.warning("Game agent couldn't understand request to make move! Reason: " + notUnderstood.getContent());
    }

    private void handleInform(ACLMessage inform) {
        logger.info("Game agent made move.");
    }

    private void handleFailure(ACLMessage failure) {
        logger.warning("Game agent failed to make move: " + failure.getContent());
    }

    @Override
    public void action() {
        ACLMessage request, response, result;

        switch (this.state) {
            case SENDING_REQUEST:
                request = prepareRequest(new ACLMessage(ACLMessage.REQUEST));
                myAgent.send(request);
                state = RECEIVE_RESPONSE;
                break;
            case RECEIVE_RESPONSE:
                response = myAgent.receive(mt);
                if (response != null) {
                    handleResponse(response);
                } else {
                    block();
                }
                break;
            case RECEIVE_RESULT:
                result = myAgent.receive(mt);
                if (result != null) {
                    handleResult(result);
                } else {
                    block();
                }
                break;
        }
    }

    private void handleResult(ACLMessage result) {
        switch (result.getPerformative()) {
            case ACLMessage.INFORM:
                handleInform(result);
                state = DONE;
                moveMade = true;
                break;
            case ACLMessage.FAILURE:
                handleFailure(result);
                state = DONE;
                break;
        }
    }

    private void handleResponse(ACLMessage response) {
        switch (response.getPerformative()) {
            case ACLMessage.AGREE:
                this.handleAgree(response);
                state = RECEIVE_RESULT;
                break;
            case ACLMessage.REFUSE:
                this.handleRefuse(response);
                state = DONE;
                break;
            case ACLMessage.NOT_UNDERSTOOD:
                this.handleNotUnderstood(response);
                state = DONE;
                break;
        }
    }

    @Override
    public boolean done() {
        return state == DONE;
    }

    public boolean wasSuccessful() {
        return moveMade;
    }
}