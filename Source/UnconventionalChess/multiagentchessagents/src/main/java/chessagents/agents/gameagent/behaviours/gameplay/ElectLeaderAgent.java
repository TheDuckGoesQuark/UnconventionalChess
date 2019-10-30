package chessagents.agents.gameagent.behaviours.gameplay;

import jade.core.behaviours.Behaviour;

public class ElectLeaderAgent extends Behaviour {

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int onEnd() {
        return GamePlayTransition.LEADER_AGENT_CHOSEN.ordinal();
    }
}
