package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import jade.core.behaviours.SimpleBehaviour;

public class InitTurn extends SimpleBehaviour {

    private final GameContext context;
    private GamePlayTransition nextState = null;

    InitTurn(GameAgent agent, GameContext context) {
        super(agent);
        this.context = context;
    }

    @Override
    public void action() {
        if (context.getBoard().gameIsOver()) {
            nextState = GamePlayTransition.GAME_COMPLETE;
        } else if (context.isHumanTurn()) {
            nextState = GamePlayTransition.IS_HUMAN_MOVE;
        } else {
            nextState = GamePlayTransition.IS_AGENT_MOVE;
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
