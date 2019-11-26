package chessagents.agents.gameagent;

import chessagents.chess.GameState;
import chessagents.agents.ChessAgent;
import chessagents.agents.gameagent.behaviours.chat.HandleChat;
import chessagents.agents.gameagent.behaviours.gameplay.HandleGame;
import chessagents.agents.gameagent.behaviours.meta.CleanupGame;
import chessagents.agents.gameagent.behaviours.meta.HandleGameCreationRequests;
import chessagents.agents.gameagent.behaviours.meta.HandleGameStatusSubscriptions;
import chessagents.agents.gameagent.behaviours.meta.SpawnPieceAgents;
import chessagents.ontology.schemas.concepts.Game;
import jade.core.AID;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;


/**
 * Game agent is responsible for acting as the single source of truth
 * for the current state of the board, as well as :
 * * forwarding moves and messages to the client
 * * initialising the piece agents
 */
public class GameAgent extends ChessAgent {

    private GameAgentContext myContext;

    @Override
    protected void setup() {
        super.setup();

        var arguments = getArguments();
        var properties = (GameProperties) arguments[0];
        var gameAgent = new AID((String) arguments[1], true);
        myContext = new GameAgentContext(gameAgent, properties);

        addBehaviour(new HandleGameStatusSubscriptions(this, myContext));
        addBehaviour(new HandleGameCreationRequests(this, myContext));
    }

    public void createGame(Game game) {
        myContext.setGameState(new GameState(game.getGameId()));

        var gameSequence = new SequentialBehaviour();
        gameSequence.addSubBehaviour(new SpawnPieceAgents(this, myContext));
        gameSequence.addSubBehaviour(new HandleGame(this, myContext));
        gameSequence.addSubBehaviour(new CleanupGame(this, myContext));

        var chatHandler = new HandleChat(this);

        // Game agent handles game logic and messaging in parallel
        // rather than adding more message handling transitions to the move FSA.
        // Both behaviours terminate when either chat fails or game ends
        var parallel = new ParallelBehaviour(ParallelBehaviour.WHEN_ANY);
        parallel.addSubBehaviour(gameSequence);
        parallel.addSubBehaviour(chatHandler);

        addBehaviour(parallel);
    }
}
