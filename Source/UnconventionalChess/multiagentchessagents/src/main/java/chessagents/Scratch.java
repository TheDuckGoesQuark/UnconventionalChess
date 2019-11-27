package chessagents;

import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.realiser.english.Realiser;

class Scratch {
    private static final Lexicon LEXICON = Lexicon.getDefaultLexicon();
    private static final NLGFactory NLG_FACTORY = new NLGFactory(LEXICON);
    private static final Realiser REALISER = new Realiser(LEXICON);

    public static void main(String[] args) {
        var clause = NLG_FACTORY.createClause("I", "agree");
        var subClause = NLG_FACTORY.createClause("I", "move", "there");
        subClause.setFeature(Feature.TENSE, Tense.FUTURE);
        subClause.setFeature(Feature.COMPLEMENTISER, "to");
        clause.setComplement(subClause);
        System.out.println(REALISER.realiseSentence(clause));
    }
}