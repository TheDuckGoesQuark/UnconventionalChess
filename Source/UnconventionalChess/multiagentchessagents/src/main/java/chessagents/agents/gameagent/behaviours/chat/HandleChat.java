package chessagents.agents.gameagent.behaviours.chat;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameAgentContext;
import jade.core.behaviours.SimpleBehaviour;

// TODO create behaviour for subscribing to chat messages and handling those subscriptions
// TODO route chat messages to gateway agent
public class HandleChat extends SimpleBehaviour {

    public static final String CHAT_PROTOCOL = "CHAT_PROTOCOL";
    private final GameAgentContext myContext;

    public HandleChat(GameAgent gameAgent, GameAgentContext myContext) {
        super(gameAgent);
        this.myContext = myContext;
    }

    @Override
    public void action() {
        // TODO
    }

    @Override
    public boolean done() {
        return false;
    }
}
