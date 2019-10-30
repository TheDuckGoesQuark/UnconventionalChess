package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;

public class RefuseMove extends Behaviour {

    public RefuseMove(GameAgent myAgent, GameContext context, DataStore datastore) {
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }
}
