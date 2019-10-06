package stacs.chessgateway.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.Profile;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.GatewayListener;
import jade.wrapper.gateway.JadeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stacs.chessgateway.config.GatewayProperties;
import stacs.chessgateway.gateway.behaviours.ReceiveMessage;
import stacs.chessgateway.models.ChatMessage;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.models.MessageType;
import stacs.chessgateway.models.MoveMessage;
import stacs.chessgateway.services.GatewayService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.*;

@Component
public class GatewayPollingDaemon implements GatewayListener, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GatewayPollingDaemon.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Queue<ACLMessage> messageQueue = new LinkedBlockingQueue<>();
    private final ObjectMapper objectMapper;
    private final GatewayService gatewayService;

    private Future task;

    @Autowired
    public GatewayPollingDaemon(ObjectMapper objectMapper, GatewayProperties gatewayProperties, GatewayService gatewayService) {
        this.objectMapper = objectMapper;
        this.gatewayService = gatewayService;

        // Initialize the JadeGateway to connect to the running JADE-based system.
        // Assuming the main container is the one we want to use
        Properties pp = new Properties();
        pp.setProperty(Profile.MAIN_HOST, gatewayProperties.getMainContainerHostName());
        pp.setProperty(Profile.MAIN_PORT, Integer.toString(gatewayProperties.getMainContainerPort()));

        // Override default gateway agent with custom class
        JadeGateway.init(null, pp);

        // Register self as event listener
        JadeGateway.addListener(this);
    }

    @Override
    public void handleGatewayConnected() {
        logger.info("Gateway connected");
        task = executorService.submit(this);
    }

    @Override
    public void handleGatewayDisconnected() {
        logger.info("Gateway disconnected");
        task.cancel(true);
    }

    @Override
    public void run() {
        while (JadeGateway.isGatewayActive()) {
            logger.info("Listening for messages from gateway");
            try {
                JadeGateway.execute(new ReceiveMessage(messageQueue));
                final ACLMessage received = messageQueue.poll();

                if (received != null) {
                    forwardMessage(received);
                }
            } catch (ControllerException | InterruptedException | IOException e) {
                logger.error("Exception when listening for messages from gateway");
                logger.error(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private void forwardMessage(ACLMessage received) throws IOException {
        final String content = received.getContent();
        final MessageType type = objectMapper.readValue(content, Message.class).getType();

        switch (type) {
            case CHAT_MESSAGE:
                gatewayService.handleAgentMessage(objectMapper.convertValue(content, ChatMessage.TYPE_REFERENCE));
                break;
            case MOVE_MESSAGE:
                gatewayService.handleAgentMessage(objectMapper.convertValue(content, MoveMessage.TYPE_REFERENCE));
                break;
        }
    }
}
