package stacs.chessgateway.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;
import stacs.chessgateway.exceptions.GatewayFailureException;
import stacs.chessgateway.models.MoveMessage;
import stacs.chessgateway.services.GatewayService;


@RestController
public class WebsocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketController.class);

    private final GatewayService gatewayService;

    @Autowired
    public WebsocketController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @SubscribeMapping("/topics/chess")

    @MessageMapping("/chess.move")
    public void handleMessage(@Payload final MoveMessage move) throws GatewayFailureException {
        logger.info("Websocket move endpoint hit!");
        gatewayService.sendMove(move);
    }

}
