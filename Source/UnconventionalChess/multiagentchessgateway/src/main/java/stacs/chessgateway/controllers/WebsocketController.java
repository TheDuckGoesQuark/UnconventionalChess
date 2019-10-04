package stacs.chessgateway.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import stacs.chessgateway.models.MoveMessage;

import java.time.Instant;

@RestController
public class WebsocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketController.class);

    @MessageMapping("/chess.move")
    @SendTo("/topic/chess")
    public MoveMessage handleMessage(@Payload final MoveMessage move) {
        logger.info("Websocket move endpoint hit!");
        return new MoveMessage(Instant.now(), move.getPieceId(), move.getFromCoords(), move.getToCoords());
    }

}
