package stacs.chessgateway.services;

import chessagents.agents.gatewayagent.MessageHandler;
import chessagents.agents.gatewayagent.messages.MoveMessage;
import jade.core.AID;
import stacs.chessgateway.exceptions.GatewayFailureException;
import stacs.chessgateway.models.GameConfiguration;
import stacs.chessgateway.models.Message;

public interface GatewayService extends MessageHandler<Message> {

    /**
     * Sends the given move to the agents
     *
     * @param move   move to send
     * @param gameId
     */
    void sendMoveToGameAgent(Message<MoveMessage> move, int gameId) throws GatewayFailureException;

    /**
     * Initialises game using the given configuration and returns
     * the configuration with the id of the created game.
     *
     * @param gameConfiguration configuration to use when creating game
     * @return configuration with game id
     */
    Message<GameConfiguration> createGame(GameConfiguration gameConfiguration) throws GatewayFailureException;

}
