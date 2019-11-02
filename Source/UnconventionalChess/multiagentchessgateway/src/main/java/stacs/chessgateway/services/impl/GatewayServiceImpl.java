package stacs.chessgateway.services.impl;

import chessagents.agents.gameagent.GameProperties;
import chessagents.agents.gatewayagent.behaviours.RequestCreateGame;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.states.SubscribeToMoves;
import jade.core.AID;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stacs.chessgateway.config.GatewayProperties;
import stacs.chessgateway.exceptions.GatewayFailureException;
import chessagents.agents.gatewayagent.behaviours.CreateGameAgent;
import chessagents.agents.commonbehaviours.RequestGameAgentMove;
import stacs.chessgateway.models.GameConfiguration;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.models.MessageType;
import stacs.chessgateway.models.MoveMessage;
import stacs.chessgateway.services.GatewayService;
import stacs.chessgateway.services.WebsocketService;
import stacs.chessgateway.util.GameContextStore;
import stacs.chessgateway.util.OntologyTranslator;

import java.util.Arrays;

@Service
public class GatewayServiceImpl implements GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    private final GameContextStore gameContextStore;
    private final WebsocketService websocketService;
    private final OntologyTranslator ontologyTranslator;
    private final String platformName;

    @Autowired
    public GatewayServiceImpl(GameContextStore gameContextStore, WebsocketService websocketService, OntologyTranslator ontologyTranslator, GatewayProperties properties) {
        this.gameContextStore = gameContextStore;
        this.websocketService = websocketService;
        this.ontologyTranslator = ontologyTranslator;
        this.platformName = properties.getPlatformName();
    }

    @Override
    public void sendMoveToGameAgent(Message<MoveMessage> move, int gameId) throws GatewayFailureException {
        try {
            var gameAgentAID = gameContextStore.getAgentByGameId(gameId);
            var makeMove = ontologyTranslator.translateToOntology(move.getBody());

            var requestGameAgentMove = new RequestGameAgentMove(makeMove, gameAgentAID);
            JadeGateway.execute(requestGameAgentMove);

            if (requestGameAgentMove.wasSuccessful()) {
                websocketService.sendMessageToClient(move, gameId);
            } else {
                logger.warn("Human move was refused!");
                // TODO send error
            }
        } catch (InterruptedException | ControllerException e) {
            throw new GatewayFailureException(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public Message<GameConfiguration> createGame(GameConfiguration gameConfiguration) throws GatewayFailureException {
        try {
            var gameId = gameContextStore.size() + 1;
            var gameAgentId = new AID("GameAgent-" + gameId + "@" + platformName, AID.ISGUID);

            var gameAgentProperties = new GameProperties(
                    gameConfiguration.isHumanPlays(),
                    gameConfiguration.isHumanPlaysAsWhite()
            );

            // Spawn GameAgent
            var createGameAgent = new CreateGameAgent(gameAgentProperties, gameAgentId);
            JadeGateway.execute(createGameAgent);

            // Request that GameAgent creates piece agents and informs when done
            var requestCreateGame = new RequestCreateGame(createGameAgent.getAgent(), gameAgentId, gameId);
            JadeGateway.execute(requestCreateGame);

            // Subscribe to moves
            var pieceContext = new PieceContext(gameId, null, gameAgentId, null, 0);
            var subscribeToMoves = new SubscribeToMoves(createGameAgent.getAgent(), pieceContext);
            JadeGateway.execute(subscribeToMoves);

            // Game creation successful, remember mapping and inform client
            gameContextStore.addMapping(gameId, gameAgentId);
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
                gameContextStore.getGameIdByAgent(agentId)
        );
    }
}
