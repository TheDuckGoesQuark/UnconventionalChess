package chessagents.agents.gameagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.commonbehaviours.CyclicReceiveMessage;
import chessagents.agents.commonbehaviours.Reply;
import chessagents.agents.gameagent.behaviours.BroadcastMadeMove;
import chessagents.agents.gameagent.behaviours.SetupGame;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Move;
import com.github.bhlangonijr.chesslib.*;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.*;

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
     * AID of gateway agent to report to
     */
    private AID gatewayAgentAID;
    /**
     * Game board
     */
    private BoardWrapper board;

    @Override
    protected void setup() {
        super.setup();
        board = new BoardWrapper();
        pieceAgents = new HashSet<>();

        logger.info("Configuring game agent " + this.getName());
        parseArguments();
        addBehaviours();
    }

    /**
     * Extracts the game agent properties and optional gateway agent AID from arguments
     */
    private void parseArguments() {
        final Object[] arguments = getArguments();
        properties = (GameAgentProperties) arguments[0];
        gatewayAgentAID = arguments.length >= 2 ? new AID((String) arguments[1], true) : null;
    }

    /**
     * Adds all relevant behaviors to this agent
     */
    private void addBehaviours() {
        // one shot behaviour to configure the game
        logger.info("Adding setup game behaviour");
        addBehaviour(new SetupGame(properties, pieceAgents, board));

        // cyclic behaviour for receiving game message
        logger.info("Adding receiving game message behaviour");
        addBehaviour(new CyclicReceiveMessage() {
            @Override
            public void handle(ACLMessage message) {
                if (isHumanAgent(message.getSender())) {
                    handleHumanMove(message);
                } else if (isKnownAgent(message.getSender())) {
                    handleAgentMove(message);
                } else {
                    myAgent.addBehaviour(new Reply(message, ACLMessage.NOT_UNDERSTOOD));
                }
            }
        });
    }

    /**
     * @param sender sender of message
     * @return true if the AID belongs to the human player gateway agent
     */
    private boolean isHumanAgent(final AID sender) {
        return sender.equals(gatewayAgentAID);
    }

    /**
     * @param sender sender of message
     * @return true if AID belongs to a piece agent
     */
    private boolean isKnownAgent(final AID sender) {
        return pieceAgents.contains(sender);
    }

    /**
     * Handle any move messages given by an piece agent
     *
     * @param message message from a piece agent
     */
    private void handleAgentMove(ACLMessage message) {
        // TODO
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
            addBehaviour(new BroadcastMadeMove(message.getConversationId(), move, pieceAgents));
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
            final MakeMove makeMove = (MakeMove) ((Action) getContentManager().extractAbsContent(message)).getAction();
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
        if (properties.isHumanPlays())
            return false;

        final String humanSide = properties.isHumanPlaysAsWhite() ? Side.WHITE.value() : Side.BLACK.value();

        return board.isSideToGo(humanSide);
    }
}
