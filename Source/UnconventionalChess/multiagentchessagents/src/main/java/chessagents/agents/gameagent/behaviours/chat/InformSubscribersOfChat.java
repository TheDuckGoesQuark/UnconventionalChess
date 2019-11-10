package chessagents.agents.gameagent.behaviours.chat;

import chessagents.agents.commonbehaviours.SubscriptionInform;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.predicates.SaidTo;
import jade.content.ContentElement;
import jade.content.abs.*;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;

public class InformSubscribersOfChat extends SubscriptionInform<SaidTo> {

    private final AbsIRE absIRE = new AbsIRE(SLVocabulary.IOTA);

    InformSubscribersOfChat() {
        // construct IRE for left hand side
        var absVariableSpeaker = new AbsVariable("speaker", ChessOntology.SAID_TO_SPEAKER);
        var absVariablePhrase = new AbsVariable("phrase", ChessOntology.SAID_TO_PHRASE);
        var variableAggregate = new AbsAggregate(SLVocabulary.SEQUENCE);
        variableAggregate.add(absVariableSpeaker);
        variableAggregate.add(absVariablePhrase);
        absIRE.setVariables(variableAggregate);

        var absProp = new AbsPredicate(ChessOntology.SAID_TO);
        absProp.set(ChessOntology.SAID_TO_SPEAKER, absVariableSpeaker);
        absProp.set(ChessOntology.SAID_TO_PHRASE, absVariablePhrase);
        absIRE.setProposition(absProp);
    }

    @Override
    public ContentElement buildInformContents(SaidTo event) {
        var equals = new AbsPredicate(BasicOntology.EQUALS);

        var absAggregate = new AbsAggregate(SLVocabulary.SEQUENCE);
        try {
            var ontology = ChessOntology.getInstance();
            var speaker = (AbsConcept) ontology.fromObject(event.getSpeaker());
            var phrase = (AbsPrimitive) ontology.fromObject(event.getPhrase());

            absAggregate.add(speaker);
            absAggregate.add(phrase);
        } catch (OntologyException e) {
            e.printStackTrace();
        }

        equals.set(BasicOntology.EQUALS_LEFT, absIRE);
        equals.set(BasicOntology.EQUALS_RIGHT, absAggregate);

        return equals;
    }

}
