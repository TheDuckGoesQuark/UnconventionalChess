package stacs.chessgateway.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.services.WebsocketService;

@Service
public class WebsocketServiceImpl implements WebsocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebsocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendMessageToClient(Message message) {
        messagingTemplate.convertAndSend("/topic/chess." + message.getGameId());
    }
}
