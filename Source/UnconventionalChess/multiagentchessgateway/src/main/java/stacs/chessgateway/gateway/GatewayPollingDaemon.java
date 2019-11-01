package stacs.chessgateway.gateway;

import chessagents.agents.gatewayagent.behaviours.CallbackOnMove;
import chessagents.agents.gatewayagent.behaviours.MessageHandler;
import chessagents.ontology.schemas.predicates.MoveMade;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.GatewayListener;
import jade.wrapper.gateway.JadeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.models.MessageType;
import stacs.chessgateway.models.MoveMessage;
import stacs.chessgateway.services.GatewayService;
import stacs.chessgateway.util.GameContextStore;
import stacs.chessgateway.util.OntologyTranslator;

import java.util.concurrent.*;

import static stacs.chessgateway.models.MessageType.MOVE_MESSAGE;

@Component
public class GatewayPollingDaemon implements GatewayListener, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GatewayPollingDaemon.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final GatewayService gatewayService;
    private final OntologyTranslator ontologyTranslator;
    private final GameContextStore gameContextStore;

    private Future task;

    @Autowired
    public GatewayPollingDaemon(GatewayService gatewayService, OntologyTranslator ontologyTranslator, GameContextStore gameContextStore) {
        this.gatewayService = gatewayService;
        this.ontologyTranslator = ontologyTranslator;
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
        logger.info("Gateway disconnected");
        task.cancel(true);
    }


    @Override
    public void run() {
        while (JadeGateway.isGatewayActive()) {
            logger.info("Listening for messages from gateway");
            var handler = new MessageHandler() {
                @Override
                public Void call() {
                    forwardMessage(this);
                    return null;
                }
            };
            try {
                JadeGateway.execute(new CallbackOnMove(handler));
            } catch (ControllerException | InterruptedException e) {
                logger.warn("Failed to start callback on move listener");
            }
        }
    }

    private void forwardMessage(MessageHandler handler) {
        var move = handler.getMove();
        var moveMessage = new MoveMessage(move.getSource().getCoordinates(), move.getTarget().getCoordinates());
        gatewayService.handleAgentMessage(new Message<>(MOVE_MESSAGE, moveMessage), handler.getAgentID());
    }
}
