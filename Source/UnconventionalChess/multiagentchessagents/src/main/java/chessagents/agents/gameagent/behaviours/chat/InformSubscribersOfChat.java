package chessagents.agents.gameagent.behaviours.chat;

import chessagents.agents.commonbehaviours.SubscriptionInform;
import chessagents.ontology.schemas.predicates.SaidTo;
import jade.content.ContentElement;

public class InformSubscribersOfChat extends SubscriptionInform<SaidTo> {

    @Override
    public ContentElement buildInformContents(SaidTo event) {
        return null;
    }
}
