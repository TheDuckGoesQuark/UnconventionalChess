package stacs.chessgateway.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.Profile;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import jade.wrapper.gateway.JadeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import stacs.chessgateway.config.GatewayProperties;
import stacs.chessgateway.exceptions.GatewayFailureException;
import stacs.chessgateway.gatewaybehaviours.ReceiveMove;
import stacs.chessgateway.gatewaybehaviours.SendMove;
import stacs.chessgateway.models.MoveMessage;
import stacs.chessgateway.services.GatewayService;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class GatewayServiceImpl implements GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final GatewayProperties gatewayProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public GatewayServiceImpl(GatewayProperties gatewayProperties, ObjectMapper objectMapper, SimpMessagingTemplate messagingTemplate) {
        this.gatewayProperties = gatewayProperties;
        this.objectMapper = objectMapper;

        // Initialize the JadeGateway to connect to the running JADE-based system.
        // Assuming the main container is the one we want to use
        Properties pp = new Properties();
        pp.setProperty(Profile.MAIN_HOST, this.gatewayProperties.getMainContainerHostName());
        pp.setProperty(Profile.MAIN_PORT, Integer.toString(this.gatewayProperties.getMainContainerPort()));

        // Override default gateway agent with custom class
        JadeGateway.init(null, pp);

        // Start listening for moves
        executorService.scheduleWithFixedDelay(() -> {
            try {
                JadeGateway.execute(new ReceiveMove(logger, objectMapper, messagingTemplate));
            } catch (ControllerException | InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.MILLISECONDS);
    }

    @Override
    public void sendMove(MoveMessage move) throws GatewayFailureException {
        try {
            final String jsonMove = objectMapper.writeValueAsString(move);
            JadeGateway.execute(new SendMove(jsonMove));
        } catch (JsonProcessingException | InterruptedException | ControllerException e) {
            throw new GatewayFailureException(Arrays.toString(e.getStackTrace()));
        }
    }
}
