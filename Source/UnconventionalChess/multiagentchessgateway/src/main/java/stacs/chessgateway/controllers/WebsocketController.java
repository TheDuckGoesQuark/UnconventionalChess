package stacs.chessgateway.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketController.class);

    @MessageMapping("/chess")
    @SendTo("/topics/chess")
    public String handleMessage(final String message) {
        logger.info("Websocket endpoint hit!");
        return message + " received!";
    }

}
