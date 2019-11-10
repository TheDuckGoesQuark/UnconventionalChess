package chessagents.agents;

import chessagents.ontology.ChessOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.util.Logger;

public abstract class ChessAgent extends Agent {

    protected Logger logger;
    private Codec codec = new SLCodec();
    private Ontology ontology = ChessOntology.getInstance();

    @Override
    protected void setup() {
        super.setup();
        logger = Logger.getMyLogger(getName());
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
    }
}
