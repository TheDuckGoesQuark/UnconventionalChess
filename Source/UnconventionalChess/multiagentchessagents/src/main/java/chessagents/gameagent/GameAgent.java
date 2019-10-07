package chessagents.gameagent;

import chessagents.gameagent.behaviours.ProcessMove;
import jade.core.Agent;

public class GameAgent extends Agent {

    private GameAgentProperties properties;

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new ProcessMove());
        properties = (GameAgentProperties) getArguments()[0];


    }
}
