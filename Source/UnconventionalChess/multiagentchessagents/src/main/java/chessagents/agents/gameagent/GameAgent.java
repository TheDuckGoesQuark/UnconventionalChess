package chessagents.agents.gameagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.gameagent.behaviours.HandleGameCreationRequests;
import chessagents.agents.gameagent.behaviours.HandleGameStatusSubscriptions;
import chessagents.agents.gameagent.behaviours.SpawnPieceAgents;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Game;
import jade.core.AID;
import jade.core.behaviours.DataStore;
import jade.util.Logger;

import java.util.HashSet;


/**
 * Game agent is responsible for acting as the single source of truth
 * for the current state of the board, as well as :
 * * forwarding moves and messages to the client
 * * initialising the piece agents
 */
public class GameAgent extends ChessAgent {

    public static final String GAME_STATUS_KEY = "GAME_STATUS";
    public static final String GATEWAY_AGENT_KEY = "GATEWAY_AGENT_AID";
    public static final String PIECE_AGENT_SET_KEY = "PIECE_AGENT_SET";
    public static final String BOARD_KEY = "BOARD";
    private static final Logger logger = Logger.getMyLogger(GameAgent.class.getName());

    private GameAgentProperties properties;
    private DataStore dataStore;

    @Override
    protected void setup() {
        super.setup();
        dataStore = new DataStore();

        var arguments = getArguments();
        properties = (GameAgentProperties) arguments[0];

        dataStore.put(GATEWAY_AGENT_KEY, new AID((String) arguments[1], true));
        dataStore.put(GAME_STATUS_KEY, GameStatus.NOT_EXIST);
        dataStore.put(PIECE_AGENT_SET_KEY, new HashSet<AID>());
        dataStore.put(BOARD_KEY, new BoardWrapper());
        addBehaviour(new HandleGameCreationRequests(this, dataStore));
        addBehaviour(new HandleGameStatusSubscriptions(this, dataStore));
    }

    public void createGame(Game game) {
        addBehaviour(new SpawnPieceAgents(properties, game, dataStore));
    }
}
