package chessagents.agents.gameagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.gameagent.behaviours.gameplay.HandleGame;
import chessagents.agents.gameagent.behaviours.meta.CleanupGame;
import chessagents.agents.gameagent.behaviours.meta.HandleGameCreationRequests;
import chessagents.agents.gameagent.behaviours.meta.HandleGameStatusSubscriptions;
import chessagents.agents.gameagent.behaviours.meta.SpawnPieceAgents;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Game;
import chessagents.ontology.schemas.concepts.Piece;
import jade.core.AID;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SequentialBehaviour;
import jade.util.Logger;

import java.util.HashMap;

import static chessagents.agents.gameagent.GameStatus.NOT_EXIST;


/**
 * Game agent is responsible for acting as the single source of truth
 * for the current state of the board, as well as :
 * * forwarding moves and messages to the client
 * * initialising the piece agents
 */
public class GameAgent extends ChessAgent {

    private static final int MAX_NUM_PIECES = 32;
    public static final String GAME_STATUS_KEY = "GAME_STATUS";
    public static final String GATEWAY_AGENT_KEY = "GATEWAY_AGENT_AID";
    public static final String AID_TO_PIECE_KEY = "AID_TO_PIECE";
    public static final String BOARD_KEY = "BOARD";

    private final Logger logger = Logger.getMyLogger(getClass().getName());

    private GameAgentProperties properties;
    private DataStore dataStore;

    @Override
    protected void setup() {
        super.setup();
        dataStore = new DataStore();

        var arguments = getArguments();
        properties = (GameAgentProperties) arguments[0];

        dataStore.put(GATEWAY_AGENT_KEY, new AID((String) arguments[1], true));
        dataStore.put(GAME_STATUS_KEY, NOT_EXIST);
        dataStore.put(AID_TO_PIECE_KEY, new HashMap<AID, Piece>(MAX_NUM_PIECES));
        dataStore.put(BOARD_KEY, new BoardWrapper());

        addBehaviour(new HandleGameStatusSubscriptions(this, dataStore));
        addBehaviour(new HandleGameCreationRequests(this, dataStore));
    }

    public void createGame(Game game) {
        var gameSequence = new SequentialBehaviour();
        gameSequence.addSubBehaviour(new SpawnPieceAgents(properties, game, dataStore));
        gameSequence.addSubBehaviour(new HandleGame(this, properties, game, dataStore));
        gameSequence.addSubBehaviour(new CleanupGame(properties, game, dataStore));
        addBehaviour(gameSequence);
    }
}
