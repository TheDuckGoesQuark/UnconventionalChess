package stacs.chessgateway.services;

import stacs.chessgateway.exceptions.GatewayFailureException;
import stacs.chessgateway.models.MoveMessage;

public interface GatewayService {

    /**
     * Sends the given move to the agents
     *
     * @param move move to send
     */
    void sendMove(MoveMessage move) throws GatewayFailureException;

}
