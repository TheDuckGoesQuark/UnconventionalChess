package stacs.chessgateway.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stacs.chessgateway.exceptions.GatewayFailureException;
import stacs.chessgateway.gateway.behaviours.CreateGame;
import stacs.chessgateway.gateway.behaviours.SendMove;
import stacs.chessgateway.models.GameConfiguration;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.models.MessageType;
import stacs.chessgateway.models.MoveMessage;
import stacs.chessgateway.services.GatewayService;
import stacs.chessgateway.services.WebsocketService;
import stacs.chessgateway.util.GameAgentMapper;

import java.util.Arrays;

@Service
public class GatewayServiceImpl implements GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    private final GameAgentMapper gameAgentMapper;
    private final WebsocketService websocketService;
    private final ObjectMapper objectMapper;

    @Autowired
    public GatewayServiceImpl(GameAgentMapper gameAgentMapper, WebsocketService websocketService, ObjectMapper objectMapper) {
        this.gameAgentMapper = gameAgentMapper;
        this.websocketService = websocketService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendMoveToGameAgents(Message<MoveMessage> move, int gameId) throws GatewayFailureException {
        try {
            final String jsonMove = objectMapper.writeValueAsString(move.getBody());
            final String agentId = gameAgentMapper.getAgentByGameId(gameId);

            logger.info("Sending move to agent " + agentId + ": " + jsonMove);

            JadeGateway.execute(new SendMove(jsonMove, agentId));
        } catch (JsonProcessingException | InterruptedException | ControllerException e) {
            throw new GatewayFailureException(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public Message<GameConfiguration> createGame(GameConfiguration gameConfiguration) throws GatewayFailureException {
        try {
            int gameId = gameAgentMapper.size() + 1;
            final String agentId = "GameAgent-" + gameId;
            gameConfiguration.setGameId(gameId);

            JadeGateway.execute(new CreateGame(gameConfiguration, agentId));

            gameAgentMapper.addMapping(gameId, agentId);

            return new Message<>(MessageType.GAME_CONFIGURATION_MESSAGE, gameConfiguration);
        } catch (InterruptedException | ControllerException e) {
            throw new GatewayFailureException(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void handleAgentMessage(Message message, String agentId) {
        switch (message.getType()) {
            case MOVE_MESSAGE:
            case CHAT_MESSAGE:
                websocketService.sendMessageToClient(message, gameAgentMapper.getGameIdByAgent(agentId));
                break;
        }
    }
}
