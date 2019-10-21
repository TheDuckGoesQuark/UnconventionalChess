package chessagents.agents.gameagent.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

import java.util.Set;

public class HandleGameMetaQueries extends SimpleAchieveREResponder {

    private static final int INITIAL_NUMBER_OF_PIECES = 32;
    private final Set<AID> pieceAgents;

    public HandleGameMetaQueries(Agent a, Set<AID> pieceAgents) {
        super(a, MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF));
        this.pieceAgents = pieceAgents;
    }

    private boolean gameIsReady() {
        return pieceAgents.size() == INITIAL_NUMBER_OF_PIECES;
    }

    private void handleQuery(ACLMessage message) {
//        var content = myAgent.getContentManager().extractContent(message);

//        if (content instanceof IsReady) {
//            andleGameReadyQuery();
//            var isReady = (IsReady) content;
//
//            if (gameIsReady()) {
//
//            } else {
//
//            }
//        }
    }
}
