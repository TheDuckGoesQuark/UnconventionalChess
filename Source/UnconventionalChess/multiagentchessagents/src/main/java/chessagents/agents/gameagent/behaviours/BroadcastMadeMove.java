package chessagents.agents.gameagent.behaviours;

import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Move;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
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
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setOntology(ChessOntology.ONTOLOGY_NAME);
        message.setConversationId(conversationId);
        agents.forEach(message::addReceiver);

        final Action action = new Action(myAgent.getAID(), move);
        myAgent.getContentManager().fillContent(message, action);

        return message;
    }

}
