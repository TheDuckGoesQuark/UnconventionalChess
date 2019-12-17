package stacs.chessgateway.services.impl;

import chessagents.agents.gameagent.GameProperties;
import chessagents.ontology.schemas.concepts.PieceConfiguration;
import chessagents.agents.gatewayagent.behaviours.RequestCreateGame;
import chessagents.agents.gatewayagent.behaviours.SubscribeToChatter;
import chessagents.agents.gatewayagent.messages.MoveMessage;
import chessagents.agents.gatewayagent.messages.OntologyTranslator;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.functionality.initial.SubscribeToMoves;
import chessagents.ontology.schemas.actions.MakeMove;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
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
import chessagents.agents.gatewayagent.messages.MessageType;
import stacs.chessgateway.services.GatewayService;
import stacs.chessgateway.services.WebsocketService;
import stacs.chessgateway.util.GameContextStore;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class GatewayServiceImpl implements GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    private final GameContextStore gameContextStore;
    private final WebsocketService websocketService;
    private final OntologyTranslator<Message> ontologyMessageMapper;
    private final String platformName;

    @Autowired
    public GatewayServiceImpl(GameContextStore gameContextStore, WebsocketService websocketService, OntologyTranslator<Message> ontologyMessageTranslator, GatewayProperties properties) {
        this.gameContextStore = gameContextStore;
        this.websocketService = websocketService;
        this.ontologyMessageMapper = ontologyMessageTranslator;
        this.platformName = properties.getPlatformName();
    }

    /**
     * Attempts to execute the given behaviour in the jade gateway agent.
     * Returns same behaviour to allow functional usage.
     *
     * @param behaviour behaviour to execute
     * @return the behaviour executed
     */
    private Behaviour executeBehaviour(Behaviour behaviour) {
        try {
            JadeGateway.execute(behaviour);
        } catch (ControllerException | InterruptedException e) {
            logger.warn("Failed to execute behaviour: " + e.getMessage());
        }

        return behaviour;
    }

    @Override
    public void sendMoveToGameAgent(Message<MoveMessage> move, int gameId) throws GatewayFailureException {
        ontologyMessageMapper.toOntology(move, MessageType.MOVE_MESSAGE)
                .map(makeMove -> new RequestGameAgentMove((MakeMove) makeMove, gameContextStore.getAgentByGameId(gameId)))
                .map(this::executeBehaviour)
                .map(command -> ((RequestGameAgentMove) command))
                .filter(RequestGameAgentMove::wasSuccessful)
                .ifPresent(wasSuccessful -> {
                    logger.info("Client move successful");
                    // don't need to send response now as we are subscribed to moves and will
                    // be informed again later
                });
        // TODO send error
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

            var pieceConfigs = gameConfiguration.getPieceConfigs().entrySet().stream()
                    .map(e -> new PieceConfiguration(e.getKey().toUpperCase(), e.getValue().getName(), e.getValue().getPersonality().getName()))
                    .collect(Collectors.toSet());

            // Request that GameAgent creates piece agents and informs when done
            var requestCreateGame = new RequestCreateGame(createGameAgent.getAgent(), gameAgentId, gameId, pieceConfigs);
            JadeGateway.execute(requestCreateGame);

            // Subscribe to moves
            var pieceContext = new PieceContext(gameAgentId);
            var subscribeToMoves = new SubscribeToMoves(pieceContext);
            JadeGateway.execute(subscribeToMoves);

            // Subscribe to chat
            var subscribeToChat = new SubscribeToChatter(gameAgentId);
            JadeGateway.execute(subscribeToChat);

            // Game creation successful, remember mapping and inform client
            gameContextStore.addMapping(gameId, gameAgentId);
            gameConfiguration.setGameId(gameId);
            return new Message<>(Instant.now(), MessageType.GAME_CONFIGURATION_MESSAGE, gameConfiguration);
        } catch (InterruptedException | ControllerException e) {
            throw new GatewayFailureException(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void handleAgentMessage(Message message, AID agentId) {
        websocketService.sendMessageToClient(message, gameContextStore.getGameIdByAgent(agentId));
    }
}
