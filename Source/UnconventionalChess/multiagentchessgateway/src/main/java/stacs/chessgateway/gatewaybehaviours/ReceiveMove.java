package stacs.chessgateway.gatewaybehaviours;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import stacs.chessgateway.models.MoveMessage;

import java.io.IOException;

public class ReceiveMove extends CyclicBehaviour {

    private Logger logger;
    private ObjectMapper objectMapper;
    private SimpMessagingTemplate messagingTemplate;

    public ReceiveMove(Logger logger, ObjectMapper objectMapper, SimpMessagingTemplate messagingTemplate) {
        this.logger = logger;
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void action() {
        try {
            final ACLMessage message = myAgent.receive();
            if (message != null) {
                final MoveMessage moveMessage = objectMapper.readValue(message.getContent(), MoveMessage.class);
                logger.info("received message");
                messagingTemplate.convertAndSend("/topic/chess", moveMessage);
            } else {
                block();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
