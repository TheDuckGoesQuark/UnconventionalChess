package chessagents.agents.gameagent.behaviours.chat;

import chessagents.agents.commonbehaviours.SubscriptionInform;
import chessagents.ontology.schemas.predicates.SaidTo;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;

public class InformSubscribersOfChat extends SubscriptionInform<SaidTo> {

    @Override
    public ContentElement buildInformContents(SaidTo event) {
        var action = new Action(myAgent.getAID(), (Concept) event);
        return new Done(action);
    }

}
