package chessagents.ontology.schemas.actions;

import chessagents.ontology.schemas.concepts.Game;
import jade.content.AgentAction;

public class CreateGame implements AgentAction {

    private Game game;

    public CreateGame() {
    }

    public CreateGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
