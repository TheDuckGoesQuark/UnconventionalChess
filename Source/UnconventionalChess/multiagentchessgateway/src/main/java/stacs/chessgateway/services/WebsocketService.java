package stacs.chessgateway.services;

import stacs.chessgateway.models.Message;

public interface WebsocketService {

    /**
     * Sends the given message to clients
     *
     * @param message message to send
     */
    void sendMessageToClient(Message message, int gameId);
}
