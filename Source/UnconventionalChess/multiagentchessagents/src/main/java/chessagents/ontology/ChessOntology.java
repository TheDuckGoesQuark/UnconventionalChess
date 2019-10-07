package chessagents.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;

public class ChessOntology extends Ontology {

    public static final String ONTOLOGY_NAME = "Chess-Ontology";

    private static final ChessOntology instance = new ChessOntology();

    private ChessOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
    }

    public static ChessOntology getInstance() {
        return instance;
    }

    // TODO
}
