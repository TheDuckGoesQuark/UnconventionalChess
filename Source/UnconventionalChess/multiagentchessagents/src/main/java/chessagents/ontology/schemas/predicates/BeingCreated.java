package chessagents.ontology.schemas.predicates;

import chessagents.ontology.schemas.concepts.Game;
import jade.content.Predicate;

public class BeingCreated implements Predicate {

    private Game game;

    public BeingCreated() {
    }

    public BeingCreated(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
