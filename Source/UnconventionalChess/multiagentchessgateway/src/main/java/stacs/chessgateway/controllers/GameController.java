package stacs.chessgateway.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import stacs.chessgateway.exceptions.GatewayFailureException;
import stacs.chessgateway.models.GameConfiguration;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.models.MoveMessage;
import stacs.chessgateway.services.GatewayService;


@RestController
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GatewayService gatewayService;

    @Autowired
    public GameController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    /**
     * WS handler method for incoming move messages
     *
     * @param move   move made
     * @param gameId id of game move was made in
     * @throws GatewayFailureException if unable to forward to move to agents
     */
    @MessageMapping("/game.{gameId}.move")
    public void handleMoveMessage(@Payload final Message<MoveMessage> move, @DestinationVariable final int gameId) throws GatewayFailureException {
        logger.info("Websocket move endpoint hit for game id " + gameId + "!");
        gatewayService.sendMoveToGameAgents(move, gameId);
    }

    /**
     * RESTFul endpoint for creating a new game.
     *
     * @param gameConfiguration configuration for game to start
     * @return gameconfiguration with gameId field populated with the id of the created game
     * @throws GatewayFailureException if unable to create the game agents
     */
    @PostMapping("/game")
    public Message<GameConfiguration> createGame(@RequestBody final Message<GameConfiguration> gameConfiguration) throws GatewayFailureException {
        logger.info("Received request to create game");
        return gatewayService.createGame(gameConfiguration.getBody());
    }

}
