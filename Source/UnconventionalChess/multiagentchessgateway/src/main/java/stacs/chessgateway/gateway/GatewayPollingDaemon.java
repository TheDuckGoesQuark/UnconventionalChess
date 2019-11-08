package stacs.chessgateway.gateway;

import chessagents.agents.gatewayagent.behaviours.ListenForGameAgentMessages;
import chessagents.agents.gatewayagent.messages.MoveMessage;
import chessagents.agents.gatewayagent.messages.OntologyTranslator;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.GatewayListener;
import jade.wrapper.gateway.JadeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.services.GatewayService;
import stacs.chessgateway.util.GameContextStore;

import java.util.concurrent.*;

import static chessagents.agents.gatewayagent.messages.MessageType.MOVE_MESSAGE;

@Component
public class GatewayPollingDaemon implements GatewayListener, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GatewayPollingDaemon.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final GatewayService gatewayService;
    private final OntologyTranslator<Message> ontologyMessageMapper;
    private final GameContextStore gameContextStore;

    private Future task;

    @Autowired
    public GatewayPollingDaemon(GatewayService gatewayService, OntologyTranslator<Message> ontologyMessageMapper, GameContextStore gameContextStore) {
        this.gatewayService = gatewayService;
        this.ontologyMessageMapper = ontologyMessageMapper;
        this.gameContextStore = gameContextStore;
        JadeGateway.addListener(this);
    }

    @Override
    public void handleGatewayConnected() {
        logger.info("Gateway connected");
        task = executorService.submit(this);
    }

    @Override
    public void handleGatewayDisconnected() {
        logger.warn("Gateway disconnected");
        task.cancel(true);
    }


    @Override
    public void run() {
        while (JadeGateway.isGatewayActive()) {
            logger.info("Listening for messages from gateway");
            var listener = new ListenForGameAgentMessages<Message>(ontologyMessageMapper, gatewayService);

            try {
                JadeGateway.execute(listener);
            } catch (ControllerException | InterruptedException e) {
                logger.warn("Failed to start callback on move listener");
            }
        }
    }

}
