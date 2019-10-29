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

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private GameContext context;

    @Override
    protected void setup() {
        super.setup();

        var arguments = getArguments();
        var properties = (GameProperties) arguments[0];
        var gameAgent = new AID((String) arguments[1], true);
        context = new GameContext(gameAgent, properties);

        addBehaviour(new HandleGameStatusSubscriptions(this, context));
        addBehaviour(new HandleGameCreationRequests(this, context));
    }

    public void createGame(Game game) {
        var gameSequence = new SequentialBehaviour();
        gameSequence.addSubBehaviour(new SpawnPieceAgents(this, context));
        gameSequence.addSubBehaviour(new HandleGame(this, context));
        gameSequence.addSubBehaviour(new CleanupGame(this, context));
        addBehaviour(gameSequence);
    }
}
