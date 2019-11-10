package chessagents.agents.gameagent.behaviours.chat;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;

import static chessagents.agents.gameagent.behaviours.chat.HandleChat.CHAT_PROTOCOL;

public class HandleChatSubscriptions extends SubscriptionResponder {
    private final InformSubscribersOfChat informSubscribersOfChat;

    public HandleChatSubscriptions(Agent a, InformSubscribersOfChat informSubscribersOfChat) {
        super(a, MessageTemplate.and(
                MessageTemplate.MatchProtocol(CHAT_PROTOCOL),
                MessageTemplate.or(
                        MessageTemplate.MatchPerformative(ACLMessage.CANCEL),
                        MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE)
                )
        ));
        this.informSubscribersOfChat = informSubscribersOfChat;
        a.addBehaviour(informSubscribersOfChat);
    }

    @Override
    protected ACLMessage handleSubscription(ACLMessage subscription) {
        var sub = createSubscription(subscription);
        informSubscribersOfChat.addSubscriber(sub);

        var agree = subscription.createReply();
        agree.setPerformative(ACLMessage.AGREE);

        return agree;
    }

    @Override
    protected ACLMessage handleCancel(ACLMessage cancel) throws FailureException {
        informSubscribersOfChat.removeSubscriber(cancel.getConversationId());
        return super.handleCancel(cancel);
    }
}
