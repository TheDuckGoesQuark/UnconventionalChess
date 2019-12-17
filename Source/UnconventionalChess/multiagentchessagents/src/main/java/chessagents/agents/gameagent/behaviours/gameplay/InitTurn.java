package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameAgentContext;
import jade.core.behaviours.SimpleBehaviour;

public class InitTurn extends SimpleBehaviour {

    private final GameAgentContext context;
    private GamePlayTransition nextState = null;

    InitTurn(GameAgent agent, GameAgentContext context) {
        super(agent);
        this.context = context;
    }

    @Override
    public void action() {
        if (context.getGameState().gameIsOver()) {
            nextState = GamePlayTransition.GAME_COMPLETE;
        } else {
            nextState = GamePlayTransition.START_TURN;
        }
    }

    @Override
    public boolean done() {
        return nextState != null;
    }

    @Override
    public int onEnd() {
        return (done() ? nextState : GamePlayTransition.GAME_COMPLETE).ordinal();
    }

    @Override
    public void reset() {
        nextState = null;
        super.reset();
    }
}
