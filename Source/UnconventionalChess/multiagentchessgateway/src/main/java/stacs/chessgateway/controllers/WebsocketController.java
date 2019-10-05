package stacs.chessgateway.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import stacs.chessgateway.exceptions.GatewayFailureException;
import stacs.chessgateway.models.MoveMessage;
import stacs.chessgateway.services.GatewayService;

import java.time.Instant;

@RestController
public class WebsocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketController.class);

    private final GatewayService gatewayService;

    @Autowired
    public WebsocketController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @MessageMapping("/chess.move")
    @SendTo("/topic/chess")
    public MoveMessage handleMessage(@Payload final MoveMessage move) throws GatewayFailureException {
        logger.info("Websocket move endpoint hit!");
        gatewayService.sendMove(move);
        return new MoveMessage(Instant.now(), move.getPiece(), move.getSourceSquare(), move.getTargetSquare());
    }

}
