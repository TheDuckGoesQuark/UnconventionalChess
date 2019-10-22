package chessagents.agents.gameagent.behaviours;

import chessagents.agents.gameagent.GameAgent;
import chessagents.ontology.ChessOntology;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public class HandlePieceListRequests extends SimpleAchieveREResponder {
    public HandlePieceListRequests(GameAgent gameAgent, DataStore dataStore) {
        super(gameAgent, MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_QUERY),
                MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
        ), dataStore);
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        // TODO send agree, or reject if game not ready
        return super.prepareResponse(request);
    }

    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
        // TODO send result of query
        return super.prepareResultNotification(request, response);
    }
}
