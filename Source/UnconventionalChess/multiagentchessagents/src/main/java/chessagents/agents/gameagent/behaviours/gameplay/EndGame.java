package chessagents.agents.gameagent.behaviours.gameplay;

import jade.core.behaviours.Behaviour;
import jade.util.Logger;

public class EndGame extends Behaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());

    @Override
    public void action() {
        logger.info("GAME OVER!");
    }

    @Override
    public boolean done() {
        return true;
    }
}
