package chessagents.ontology.schemas.concepts;

import jade.content.Concept;

public class Game implements Concept {

    int gameId;

    public Game() {
    }

    public Game(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}

