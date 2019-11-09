package chessagents.agents.gameagent.behaviours.chat;

import chessagents.agents.commonbehaviours.SubscriptionInform;
import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameAgentContext;
import chessagents.agents.gameagent.behaviours.gameplay.HandleMoveSubscriptions;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.util.Logger;

// TODO create behaviour for subscribing to chat messages and handling those subscriptions
// TODO route chat messages to gateway agent
public class HandleChat extends SimpleBehaviour {

    public static final String CHAT_PROTOCOL = "CHAT_PROTOCOL";
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final InformSubscribersOfChat informSubscribersOfChat;
    private final HandleChatSubscriptions handleChatSubscriptions;

    public HandleChat(GameAgent gameAgent) {
        super(gameAgent);
        this.informSubscribersOfChat = new InformSubscribersOfChat();
        this.handleChatSubscriptions = new HandleChatSubscriptions(gameAgent, informSubscribersOfChat);
        gameAgent.addBehaviour(informSubscribersOfChat);
        gameAgent.addBehaviour(handleChatSubscriptions);
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }

    private void propagateChatToSubscribers() {
    }
}
