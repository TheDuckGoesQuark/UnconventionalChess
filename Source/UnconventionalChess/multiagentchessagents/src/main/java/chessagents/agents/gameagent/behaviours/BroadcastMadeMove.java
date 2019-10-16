package chessagents.agents.gameagent.behaviours;

import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Move;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.Set;

public class BroadcastMadeMove extends OneShotBehaviour {

    private static final Logger logger = Logger.getJADELogger(BroadcastMadeMove.class.getName());

    private final String conversationId;
    private final MakeMove move;
    private final Set<AID> agents;

    public BroadcastMadeMove(String conversationId, Move move, Set<AID> agents) {
        this.conversationId = conversationId;
        this.move = new MakeMove(move);
        this.agents = agents;
    }

    @Override
    public void action() {
        try {
            myAgent.send(constructMessage());
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Unable to broadcast move: " + e.getMessage());
        }
    }

    private ACLMessage constructMessage() throws Codec.CodecException, OntologyException {
        final ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setSender(myAgent.getAID());
        message.setConversationId(conversationId);
        myAgent.getContentManager().fillContent(message, move);
        agents.forEach(message::addReceiver);
        return message;
    }

}
