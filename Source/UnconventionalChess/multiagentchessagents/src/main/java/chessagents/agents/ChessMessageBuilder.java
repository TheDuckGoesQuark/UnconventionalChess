package chessagents.agents;

import chessagents.ontology.ChessOntology;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.UUID;

public class ChessMessageBuilder {

    /**
     * Construct the default message with a random conversation ID, chess ontology, and SL language.
     *
     * @param performative performative of message to create
     * @return constructed message template
     */
    public static ACLMessage constructMessage(int performative) {
        var message = new ACLMessage(performative);
        message.setOntology(ChessOntology.ONTOLOGY_NAME);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setConversationId(UUID.randomUUID().toString());
        return message;
    }

}
