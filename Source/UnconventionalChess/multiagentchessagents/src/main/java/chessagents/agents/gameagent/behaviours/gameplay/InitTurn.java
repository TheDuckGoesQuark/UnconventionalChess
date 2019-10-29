package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import jade.core.behaviours.SimpleBehaviour;

import static chessagents.ontology.schemas.concepts.Colour.BLACK;
import static chessagents.ontology.schemas.concepts.Colour.WHITE;

public class InitTurn extends SimpleBehaviour {

    private final GameContext context;
    private GamePlayTransition nextState = null;

    InitTurn(GameAgent agent, GameContext context) {
        super(agent);
        this.context = context;
    }

    private boolean isHumanTurn() {
        var board = context.getBoard();
        var gameProperties = context.getGameProperties();

        return gameProperties.isHumanPlays() &&
                (gameProperties.isHumanPlaysAsWhite() ? board.isSideToGo(WHITE) : board.isSideToGo(BLACK));
    }

    @Override
    public void action() {
        if (context.getBoard().gameIsOver()) {
            nextState = GamePlayTransition.GAME_COMPLETE;
        } else if (isHumanTurn()) {
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
