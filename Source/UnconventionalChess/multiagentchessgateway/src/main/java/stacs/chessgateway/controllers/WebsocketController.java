package stacs.chessgateway.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import stacs.chessgateway.models.ChatMessage;
import stacs.chessgateway.models.BaseMessage;
import stacs.chessgateway.models.MoveMessage;

import java.time.Instant;

@RestController
public class WebsocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketController.class);

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chess")
    public BaseMessage handleMessage(@Payload final BaseMessage message) {
        logger.info("Websocket endpoint hit!");

        final BaseMessage response;
        switch (message.getType()) {
            case ChatMessage.MESSAGE_TYPE:
                response = new ChatMessage(Instant.now(), "the-server", "got your message bucko");
                break;
            case MoveMessage.MESSAGE_TYPE:
                MoveMessage request = (MoveMessage) message;
                response = new MoveMessage(Instant.now(), request.getPieceId(), request.getFromCoords(), request.getToCoords());
                break;
            default:
                response = new ChatMessage(Instant.now(), "the-server", "Didn't understand that?");
                break;
        }

        return response;
    }

}
