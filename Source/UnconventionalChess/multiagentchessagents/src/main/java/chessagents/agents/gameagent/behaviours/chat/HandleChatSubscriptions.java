package chessagents.agents.gameagent.behaviours.chat;

import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;

import static chessagents.agents.gameagent.behaviours.chat.HandleChat.CHAT_PROTOCOL;

public class HandleChatSubscriptions extends SubscriptionResponder {
    private final InformSubscribersOfChat informSubscribersOfChat;

    public HandleChatSubscriptions(Agent a, InformSubscribersOfChat informSubscribersOfChat) {
        super(a, MessageTemplate.MatchProtocol(CHAT_PROTOCOL));
        this.informSubscribersOfChat = informSubscribersOfChat;
    }
}
