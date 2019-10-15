package chessagents.agents.gameagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.commonbehaviours.CyclicReceiveMessage;
import chessagents.agents.commonbehaviours.ReplyNotUnderstood;
import chessagents.agents.gameagent.behaviours.SetupGame;
import com.github.bhlangonijr.chesslib.*;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import java.util.*;

/**
 * Game agent is responsible for acting as the single source of truth
 * for the current state of the board, as well as :
 * * forwarding moves and messages to the client
 * * initialising the piece agents
 */
public class GameAgent extends ChessAgent {

    public static final String MOVE_CONVERSATION_ID = "move";
    private static final Logger logger = Logger.getMyLogger(GameAgent.class.getName());

    /**
     * Game agent properties such as which turns are agent controlled
     */
    private GameAgentProperties properties;

    /**
     * Map of side to name of all agents on that side
     */
    private Map<Side, Set<AID>> agentsBySide;
    private AID gatewayAgentAID;

    /**
     * Game board
     */
    private Board board;

    @Override
    protected void setup() {
        super.setup();
        board = new Board();
        agentsBySide = new HashMap<>();

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
        addBehaviour(new SetupGame(properties, agentsBySide, board));

        // cyclic behaviour for receiving game message
        logger.info("Adding receiving game message behaviour");
        addBehaviour(new CyclicReceiveMessage(MessageTemplate.MatchConversationId(MOVE_CONVERSATION_ID)) {
            @Override
            public void handle(ACLMessage message) {
                if (message.getSender().equals(gatewayAgentAID)) {
                    handleHumanMove(message);
                } else if (knownAgent(message.getSender())) {
                    handleAgentMove(message);
                } else {
                    myAgent.addBehaviour(new ReplyNotUnderstood(message));
                }
            }
        });
    }

    private boolean knownAgent(final AID sender) {
        return agentsBySide.values()
                .stream()
                .anyMatch(agents -> agents.contains(sender));
    }

    /**
     * Handle any move messages given by an piece agent
     *
     * @param message
     */
    private void handleAgentMove(ACLMessage message) {
        // TODO
    }

    /**
     * Handle any move messages given by the human
     *
     * @param message
     */
    private void handleHumanMove(ACLMessage message) {
        // TODO check human is playing, human turn, move valid, broadcast
    }
}
