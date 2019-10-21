package chessagents.agents.gameagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.commonbehaviours.Reply;
import chessagents.agents.gameagent.behaviours.HandleGameCreationRequests;
import chessagents.agents.gameagent.behaviours.SpawnPieceAgents;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Game;
import chessagents.ontology.schemas.concepts.Move;
import com.github.bhlangonijr.chesslib.*;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Game agent is responsible for acting as the single source of truth
 * for the current state of the board, as well as :
 * * forwarding moves and messages to the client
 * * initialising the piece agents
 */
public class GameAgent extends ChessAgent {

    private static final Logger logger = Logger.getMyLogger(GameAgent.class.getName());

    /**
     * Game agent properties such as which turns are agent controlled
     */
    private GameAgentProperties properties;
    /**
     * Map of side to name of all agents on that side
     */
    private Set<AID> pieceAgents;
    /**
     * AID of gateway (human) agent to report to
     */
    private Set<AID> gatewayAgents;
    /**
     * Game board
     */
    private BoardWrapper board;

    @Override
    protected void setup() {
        super.setup();
        board = new BoardWrapper();
        pieceAgents = new HashSet<>();
        gatewayAgents = new HashSet<>();

        var arguments = getArguments();
        properties = (GameAgentProperties) arguments[0];

        if (arguments.length > 1) {
            gatewayAgents.add(new AID((String) arguments[1], true));
        }

        addBehaviour(new HandleGameCreationRequests(this));
    }

    /**
     * Handle any move messages given by the human
     *
     * @param message message from human player
     */
    private void handleHumanMove(ACLMessage message) {
        final Optional<Move> optMove = extractMove(message);
        if (!isHumansTurn() || message.getPerformative() != ACLMessage.PROPOSE || optMove.isEmpty()) {
            addBehaviour(new Reply(message, ACLMessage.NOT_UNDERSTOOD));
        } else {
            handleMove(optMove.get(), message);
        }
    }

    /**
     * Validate and either agree to move or refuse
     *
     * @param move    move to consider
     * @param message message that contained original move
     */
    private void handleMove(Move move, ACLMessage message) {
        if (board.isValidMove(move.getSource().getCoordinates(), move.getTarget().getCoordinates())) {
            board.makeMove(move.getSource().getCoordinates(), move.getTarget().getCoordinates());

            final Set<AID> relevantAgents = new HashSet<>();
            relevantAgents.addAll(pieceAgents);
            relevantAgents.addAll(gatewayAgents);
            // TODO tell everyone
        } else {
            addBehaviour(new Reply(message, ACLMessage.REFUSE));
        }
    }

    /**
     * Attempts to extract move from ACLMessage
     *
     * @param message message containing move
     * @return optional containing parsed move (or empty if unable to parse move)
     */
    private Optional<Move> extractMove(ACLMessage message) {
        try {
            final MakeMove makeMove = (MakeMove) ((Action) getContentManager().extractContent(message)).getAction();
            return Optional.ofNullable(makeMove.getMove());
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Unable to extract move from message");
            return Optional.empty();
        }
    }

    /**
     * @return true if human is next to move
     */
    private boolean isHumansTurn() {
        if (!properties.isHumanPlays())
            return false;

        final String humanSide = properties.isHumanPlaysAsWhite() ? Side.WHITE.value() : Side.BLACK.value();

        return board.isSideToGo(humanSide);
    }

    public CompletableFuture<Game> createGame(Game game) {
        var gameReadyFuture = new CompletableFuture<Game>();
        var b = new SpawnPieceAgents(properties, pieceAgents, board, game, gameReadyFuture);
        this.addBehaviour(b);
        return gameReadyFuture;
    }
}
