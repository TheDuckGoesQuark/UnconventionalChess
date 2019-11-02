package chessagents.agents;

import chessagents.ontology.ChessOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public abstract class ChessAgent extends Agent {

    private Codec codec = new SLCodec();
    private Ontology ontology = ChessOntology.getInstance();

    @Override
    protected void setup() {
        super.setup();
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
    }

    public ACLMessage constructMessage(int performative) {
        var message = new ACLMessage(performative);
        message.setOntology(ontology.getName());
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        return message;
    }
}
