package chessagents;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.predicates.IsColour;
import jade.content.ContentManager;
import jade.content.abs.*;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.OntologyException;
import jade.content.schema.IRESchema;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import static chessagents.ontology.ChessOntology.*;
import static jade.content.onto.Ontology.checkIsTerm;

class Scratch {
    public static void main(String[] args) throws Codec.CodecException, OntologyException {
        // query for all x where isColour(x, <mycolour>)
        var ontology = ChessOntology.getInstance();
        var colour = new Colour("BLACK");

        // create abstract descriptor for colour
        AbsConcept absColour = null;
        try {
            absColour = (AbsConcept) ontology.fromObject(colour);
        } catch (OntologyException e) {
            e.printStackTrace();
        }
        absColour.set(COLOUR_COLOUR, colour.getColour());

        // declare variable in our query to be of type piece
        var absX = new AbsVariable("x", ChessOntology.PIECE);

        // query for all pieces with my colour (i.e. piece is variable)
        var absIsColour = new AbsPredicate(ChessOntology.IS_COLOUR);
        absIsColour.set(ChessOntology.IS_COLOUR_PIECE, absX);
        absIsColour.set(ChessOntology.IS_COLOUR_COLOUR, absColour);

        // Match ALL for which predicate is true
        var absIRE = new AbsIRE(SLVocabulary.ALL);
        absIRE.setVariable(absX);
        absIRE.setProposition(absIsColour);

        ContentManager contentManager = new ContentManager();
        contentManager.registerLanguage(new SLCodec());
        contentManager.registerOntology(ChessOntology.getInstance(), ChessOntology.ONTOLOGY_NAME);

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setOntology(ChessOntology.ONTOLOGY_NAME);
        contentManager.fillContent(message, absIRE);

        try {
            var content = contentManager.extractAbsContent(message);

            if (content instanceof AbsIRE) {
                var prop =  ChessOntology.getInstance().fromObject(((AbsIRE) content).getProposition());
                var absObject = prop.getAbsObject(IS_COLOUR_COLOUR);
                var actualColour = (Colour) ChessOntology.getInstance().toObject(absObject);

                var a = (AbsTerm) ontology.fromObject(actualColour);
                System.out.println(a);
            }

        } catch (Codec.CodecException e) {
            e.printStackTrace();
        } catch (OntologyException e) {
            e.printStackTrace();
        }
    }

}