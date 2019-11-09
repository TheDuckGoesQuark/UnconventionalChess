package chessagents.agents.gameagent.behaviours.chat;

import chessagents.agents.commonbehaviours.SubscriptionInform;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.predicates.SaidTo;
import jade.content.ContentElement;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsVariable;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;

public class InformSubscribersOfChat extends SubscriptionInform<SaidTo> {

    private final AbsIRE absIRE = new AbsIRE(SLVocabulary.IOTA);

    InformSubscribersOfChat() {
        // construct IRE for left hand side
        var absVariableSpeaker = new AbsVariable("speaker", ChessOntology.SAID_TO_SPEAKER);
        var absVariablePhrase = new AbsVariable("phrase", ChessOntology.SAID_TO_PHRASE);
        var variableAggregate = new AbsAggregate(SLVocabulary.SEQUENCE);
        absIRE.setVariables(variableAggregate);

        var absProp = new AbsPredicate(ChessOntology.SAID_TO);
        absProp.set(ChessOntology.SAID_TO_SPEAKER, absVariableSpeaker);
        absProp.set(ChessOntology.SAID_TO_PHRASE, absVariablePhrase);
        absIRE.setProposition(absProp);
    }

    @Override
    public ContentElement buildInformContents(SaidTo event) {
        var equals = new AbsPredicate(BasicOntology.EQUALS);

        equals.set(BasicOntology.EQUALS_LEFT, absIRE);
        equals.set(BasicOntology.EQUALS_RIGHT, equals);

        return equals;
    }

}
