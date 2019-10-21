package stacs.chessgateway.services.impl;

import chessagents.agents.gameagent.GameAgentProperties;
import chessagents.agents.pieceagent.behaviours.RequestCreateGame;
import jade.core.AID;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stacs.chessgateway.exceptions.GatewayFailureException;
import chessagents.agents.gatewayagent.behaviours.CreateGameAgent;
import chessagents.agents.gatewayagent.behaviours.SendMoveToGameAgent;
import stacs.chessgateway.models.GameConfiguration;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.models.MessageType;
import stacs.chessgateway.models.MoveMessage;
import stacs.chessgateway.services.GatewayService;
import stacs.chessgateway.services.WebsocketService;
import stacs.chessgateway.util.GameAgentMapper;
import stacs.chessgateway.util.OntologyTranslator;

import java.util.Arrays;

@Service
public class GatewayServiceImpl implements GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    private final GameAgentMapper gameAgentMapper;
    private final WebsocketService websocketService;
    private final OntologyTranslator ontologyTranslator;

    @Autowired
    public GatewayServiceImpl(GameAgentMapper gameAgentMapper, WebsocketService websocketService, OntologyTranslator ontologyTranslator) {
        this.gameAgentMapper = gameAgentMapper;
        this.websocketService = websocketService;
        this.ontologyTranslator = ontologyTranslator;
    }

    @Override
    public void sendMoveToGameAgents(Message<MoveMessage> move, int gameId) throws GatewayFailureException {
        try {
            var agentId = gameAgentMapper.getAgentByGameId(gameId);
            var makeMove = ontologyTranslator.translateToOntology(move.getBody());

            logger.info("Sending move to agent " + agentId + ": " + move.getBody().toString());

            JadeGateway.execute(new SendMoveToGameAgent(makeMove, agentId));
        } catch (InterruptedException | ControllerException e) {
            throw new GatewayFailureException(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public Message<GameConfiguration> createGame(GameConfiguration gameConfiguration) throws GatewayFailureException {
        try {
            var gameId = gameAgentMapper.size() + 1;
            var agentId = new AID("GameAgent-" + gameId, false);

            var gameAgentProperties = new GameAgentProperties(
                    gameConfiguration.isHumanPlays(),
                    gameConfiguration.isHumanPlaysAsWhite()
            );

            // Spawn GameAgent
            var createGameAgent = new CreateGameAgent(gameAgentProperties, agentId);
            JadeGateway.execute(createGameAgent);

            // Request that GameAgent creates piece agents and informs when done
            var requestCreateGame = new RequestCreateGame(createGameAgent.getAgent(), agentId, gameId);
            JadeGateway.execute(requestCreateGame);

            // Game creation successful, remember mapping and inform client
            gameAgentMapper.addMapping(gameId, agentId);
            gameConfiguration.setGameId(gameId);
            return new Message<>(MessageType.GAME_CONFIGURATION_MESSAGE, gameConfiguration);
        } catch (InterruptedException | ControllerException e) {
            throw new GatewayFailureException(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void handleAgentMessage(Message message, AID agentId) {
        websocketService.sendMessageToClient(
                message,
                gameAgentMapper.getGameIdByAgent(agentId)
        );
    }
}
