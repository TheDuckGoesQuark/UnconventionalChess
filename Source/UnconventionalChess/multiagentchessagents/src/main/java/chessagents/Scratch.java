package chessagents;

import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import chessagents.util.RandomUtil;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import simplenlg.features.*;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

import java.util.Arrays;
import java.util.Optional;

import static chessagents.agents.pieceagent.nlg.NLGUtil.NLG_FACTORY;

class Scratch {
    private static final Lexicon LEXICON = Lexicon.getDefaultLexicon();
    private static final NLGFactory NLG_FACTORY = new NLGFactory(LEXICON);
    private static final Realiser REALISER = new Realiser(LEXICON);

    public static void main(String[] args) {
//        var clause = NLG_FACTORY.createClause("I", "agree");
//        var subClause = NLG_FACTORY.createClause("I", "move", "there");
//        subClause.setFeature(Feature.TENSE, Tense.FUTURE);
//        subClause.setFeature(Feature.COMPLEMENTISER, "to");
//        clause.setComplement(subClause);
//        System.out.println(REALISER.realiseSentence(clause));
//
//        var we = NLG_FACTORY.createNounPhrase("us");
//        var doVerb = NLG_FACTORY.createVerbPhrase("doing");
//        var next = NLG_FACTORY.createNounPhrase("next");
//
        var sentence = NLG_FACTORY.createClause();
//        sentence.setSubject(we);
//        sentence.setObject(next);
//        sentence.setVerb(doVerb);
//        sentence.setFeature(Feature.TENSE, Tense.PRESENT);
//        sentence.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
//        sentence.setFeature(Feature.MODAL, "should");
//


//        var sentence = NLG_FACTORY.createClause();

        var theMove = NLG_FACTORY.createNounPhrase("move");
        theMove.setDeterminer("that");
        sentence.setObject(theMove);

        var me = NLG_FACTORY.createNounPhrase("me");
        sentence.setSubject(me);

        if (true) {
            var verb = new RandomUtil<String>().chooseRandom(Arrays.asList("rescue", "protect", "keep safe"));
            sentence.setVerb(verb);
        } else {
            var verb = new RandomUtil<String>().chooseRandom(Arrays.asList("kill", "", "keep safe"));
            sentence.setVerb(verb);
        }

        System.out.println(Optional.of(REALISER.realiseSentence(sentence)).get());
    }
}