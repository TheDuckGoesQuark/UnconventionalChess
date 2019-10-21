package stacs.chessgateway.gateway;

import jade.core.Profile;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Properties;
import jade.wrapper.gateway.GatewayListener;
import jade.wrapper.gateway.JadeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stacs.chessgateway.config.GatewayProperties;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.services.GatewayService;
import stacs.chessgateway.util.OntologyTranslator;

import java.util.Queue;
import java.util.concurrent.*;

import static stacs.chessgateway.models.MessageType.MOVE_MESSAGE;

@Component
public class GatewayPollingDaemon implements GatewayListener, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GatewayPollingDaemon.class);
    private static final String GATEWAY_AGENT_CLASS_NAME = "chessagents.agents.gatewayagent.ChessGatewayAgent";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Queue<ACLMessage> messageQueue = new LinkedBlockingQueue<>();
    private final GatewayService gatewayService;
    private final OntologyTranslator ontologyTranslator;

    private Future task;

    @Autowired
    public GatewayPollingDaemon(GatewayProperties gatewayProperties, GatewayService gatewayService, OntologyTranslator ontologyTranslator) {
        this.gatewayService = gatewayService;
        this.ontologyTranslator = ontologyTranslator;

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
        // TODO this might be stealing messages from other listeners
//        while (JadeGateway.isGatewayActive()) {
//            logger.info("Listening for messages from gateway");
//            try {
//                JadeGateway.execute(new ReceiveMessageIntoQueue(messageQueue));
//                final ACLMessage received = messageQueue.poll();
//
//                if (received != null) {
//                    forwardMessage(received);
//                }
//            } catch (ControllerException | InterruptedException e) {
//                logger.error("Exception when listening for messages from gateway");
//                logger.error(Arrays.toString(e.getStackTrace()));
//            }
//        }
    }

    private void forwardMessage(ACLMessage received) {
        ontologyTranslator.translateFromOntology(received)
                .map(moveMessage -> new Message<>(MOVE_MESSAGE, moveMessage))
                .ifPresent(moveMessage -> gatewayService.handleAgentMessage(moveMessage, received.getSender()));
    }
}
