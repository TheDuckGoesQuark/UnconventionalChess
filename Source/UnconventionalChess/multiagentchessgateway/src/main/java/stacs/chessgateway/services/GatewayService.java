package stacs.chessgateway.services;

import stacs.chessgateway.exceptions.GatewayFailureException;
import stacs.chessgateway.models.GameConfiguration;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.models.MoveMessage;

public interface GatewayService {

    /**
     * Sends the given move to the agents
     *
     * @param move move to send
     */
    void sendMoveToGameAgents(Message<MoveMessage> move) throws GatewayFailureException;

    /**
     * Initialises game using the given configuration and returns
     * the configuration with the id of the created game.
     *
     * @param gameConfiguration configuration to use when creating game
     * @return configuration with game id
     */
    Message<GameConfiguration> createGame(GameConfiguration gameConfiguration) throws GatewayFailureException;

    /**
     * Handles message that originated from an agent
     *
     * @param message message received from an agent
     */
    void handleAgentMessage(Message message);

}
