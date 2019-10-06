package chessagents.gameagent;

import jade.core.Agent;

public class GameAgent extends Agent {

    private GameAgentProperties properties;

    @Override
    protected void setup() {
        super.setup();
        properties = (GameAgentProperties) getArguments()[0];
    }
}
