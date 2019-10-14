package chessagents.agents.gameagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.gameagent.behaviours.HandleAgentMove;
import chessagents.agents.gameagent.behaviours.HandleHumanMove;
import chessagents.agents.gameagent.behaviours.ReceiveGameMessage;
import chessagents.agents.gameagent.behaviours.SetupGame;
import chessagents.agents.pieceagent.*;
import com.github.bhlangonijr.chesslib.*;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.ContainerID;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.util.Logger;
import jade.wrapper.ControllerException;

import java.util.*;
import java.util.stream.Stream;

/**
 * Game agent is responsible for acting as the single source of truth
 * for the current state of the board, as well as :
 * * forwarding moves and messages to the client
 * * initialising the piece agents
 */
public class GameAgent extends ChessAgent {

    private static final Logger logger = Logger.getMyLogger(GameAgent.class.getName());
    private GameAgentProperties properties;
    private Map<Side, Set<String>> agentsBySide;
    private Board board;

    @Override
    protected void setup() {
        super.setup();
        properties = (GameAgentProperties) getArguments()[0];
        board = new Board();
        agentsBySide = new HashMap<>();

        addBehaviour(new SetupGame());
        addBehaviour(new ReceiveGameMessage(new HandleHumanMove(), new HandleAgentMove(), ""));
    }
}
