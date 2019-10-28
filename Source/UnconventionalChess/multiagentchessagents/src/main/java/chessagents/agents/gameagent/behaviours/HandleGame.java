package chessagents.agents.gameagent.behaviours;

import chessagents.agents.gameagent.GameAgentProperties;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Game;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.util.Logger;

import static chessagents.ontology.schemas.concepts.Colour.BLACK;
import static chessagents.ontology.schemas.concepts.Colour.WHITE;

public class HandleGame extends SimpleBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final GameAgentProperties properties;
    private final Game game;
    private final BoardWrapper board;
    private final DataStore dataStore;

    public HandleGame(GameAgentProperties properties, Game game, BoardWrapper board, DataStore dataStore) {
        this.properties = properties;
        this.game = game;
        this.board = board;
        this.dataStore = dataStore;
    }

    private boolean isHumanTurn() {
        return properties.isHumanPlays() &&
                (properties.isHumanPlaysAsWhite() ? board.isSideToGo(WHITE) : board.isSideToGo(BLACK));
    }

    @Override
    public void action() {
        if (isHumanTurn()) {

        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
