package chessagents.agents.gameagent.behaviours.chat;

import chessagents.agents.commonbehaviours.SubscriptionInform;
import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameAgentContext;
import chessagents.agents.gameagent.behaviours.gameplay.HandleMoveSubscriptions;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;

// TODO create behaviour for subscribing to chat messages and handling those subscriptions
// TODO route chat messages to gateway agent
public class HandleChat extends SubscriptionResponder {

    public static final String CHAT_PROTOCOL = "CHAT_PROTOCOL";
    private final SubscriptionInform informSubscribersOfChat;

    public HandleChat(GameAgent gameAgent) {
        super(gameAgent, MessageTemplate.MatchProtocol(CHAT_PROTOCOL));
        this.informSubscribersOfChat = new InformSubscribersOfChat();
    }

}
