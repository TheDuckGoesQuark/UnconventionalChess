package chessagents.agents.gatewayagent.behaviours;

import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.MakeMove;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import static jade.content.lang.Codec.CodecException;

public class SendMoveToGameAgent extends OneShotBehaviour {

    private static final String ONTOLOGY_NAME = ChessOntology.getInstance().getName();
    private final String gameAgentName;
    private final MakeMove move;
    private final ACLMessage message;

    public SendMoveToGameAgent(MakeMove move, String gameAgentName) {
        this.move = move;
        this.gameAgentName = gameAgentName;
        this.message = new ACLMessage(ACLMessage.PROPOSE);
    }

    private void constructMessage() throws CodecException, OntologyException {
        final AID gameAgent = new AID(gameAgentName, false);

        message.addReceiver(gameAgent);
        message.setConversationId("human-move");
        message.setOntology(ONTOLOGY_NAME);
        message.setLanguage(myAgent.getContentManager().getLanguageNames()[0]);

        final Action action = new Action(myAgent.getAID(), move);
        myAgent.getContentManager().fillContent(message, action);
    }

    @Override
    public void action() {
        try {
            constructMessage();
            myAgent.send(message);
        } catch (CodecException | OntologyException e) {
            e.printStackTrace();
        }
    }
}
