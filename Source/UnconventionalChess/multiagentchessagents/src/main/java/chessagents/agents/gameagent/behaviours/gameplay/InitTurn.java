package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import jade.core.behaviours.Behaviour;

import static chessagents.ontology.schemas.concepts.Colour.BLACK;
import static chessagents.ontology.schemas.concepts.Colour.WHITE;

public class InitTurn extends Behaviour {

    private final GameContext context;

    public InitTurn(GameAgent agent, GameContext context) {
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

    }

    @Override
    public boolean done() {
        return false;
    }
}
