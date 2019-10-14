package chessagents.agents.gameagent.behaviours;

import chessagents.agents.commonbehaviours.CyclicReceiveMessage;
import chessagents.ontology.ChessOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

public class ReceiveGameMessage extends CyclicReceiveMessage {

    private static final Logger logger = Logger.getJADELogger(ReceiveGameMessage.class.getName());
    private final HandleHumanMove handleHumanMove;
    private final HandleAgentMove handleAgentMove;
    private final String humanAgentName;

    public ReceiveGameMessage(HandleHumanMove handleHumanMove, HandleAgentMove handleAgentMove, String humanAgentName) {
        super(MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME));
        this.handleHumanMove = handleHumanMove;
        this.handleAgentMove = handleAgentMove;
        this.humanAgentName = humanAgentName;
    }

    @Override
    public void handle(ACLMessage message) {
        logger.info("Received message: " + message.toString());
        if (message.getSender().getName().equals(humanAgentName)) {
            myAgent.addBehaviour(handleHumanMove);
        } else {
            myAgent.addBehaviour(handleAgentMove);
        }
    }
}
