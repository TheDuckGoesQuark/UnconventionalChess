package chessagents.agents.gameagent.behaviours;

import chessagents.agents.gameagent.GameAgentProperties;
import chessagents.ontology.schemas.concepts.Game;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;

public class HandleGame extends SimpleBehaviour {

    private final GameAgentProperties properties;
    private final Game game;
    private final DataStore dataStore;

    public HandleGame(GameAgentProperties properties, Game game, DataStore dataStore) {
        this.properties = properties;
        this.game = game;
        this.dataStore = dataStore;
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }
}
