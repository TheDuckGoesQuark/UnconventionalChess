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
import chessagents.agents.gatewayagent.behaviours.ReceiveMessageIntoQueue;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.models.MoveMessage;
import stacs.chessgateway.services.GatewayService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.*;

import static stacs.chessgateway.models.MessageType.MOVE_MESSAGE;

@Component
public class GatewayPollingDaemon implements GatewayListener, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GatewayPollingDaemon.class);
    private static final String GATEWAY_AGENT_CLASS_NAME = "chessagents.agents.gatewayagent.ChessGatewayAgent";

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
        JadeGateway.init(GATEWAY_AGENT_CLASS_NAME, pp);

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
                JadeGateway.execute(new ReceiveMessageIntoQueue(messageQueue));
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
        final String senderName = received.getSender().getLocalName();

        final MoveMessage moveMessage = objectMapper.readValue(content, MoveMessage.class);

        gatewayService.handleAgentMessage(
                new Message<>(MOVE_MESSAGE, moveMessage),
                senderName
        );
    }
}
