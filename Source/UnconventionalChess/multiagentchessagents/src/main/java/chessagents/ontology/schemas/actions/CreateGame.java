package chessagents.ontology.schemas.actions;

import jade.content.AgentAction;

public class CreateGame implements AgentAction {

    private int gameId;

    public CreateGame() {
    }

    public CreateGame(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
