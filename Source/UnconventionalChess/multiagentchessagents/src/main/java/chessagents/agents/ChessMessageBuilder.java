package chessagents.agents;

import chessagents.ontology.ChessOntology;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public interface ChessMessageBuilder {

    default ACLMessage constructMessage(int performative) {
        var message = new ACLMessage(performative);
        message.setOntology(ChessOntology.ONTOLOGY_NAME);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        return message;
    }

}
