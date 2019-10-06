package stacs.chessgateway.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.services.WebsocketService;

@Service
public class WebsocketServiceImpl implements WebsocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketServiceImpl.class);
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebsocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendMessageToClient(Message message, int gameId) {
        messagingTemplate.convertAndSend("/topic/game." + gameId, message);
    }
}
