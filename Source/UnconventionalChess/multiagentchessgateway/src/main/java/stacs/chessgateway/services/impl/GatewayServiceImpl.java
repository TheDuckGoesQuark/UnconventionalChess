package stacs.chessgateway.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.Profile;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stacs.chessgateway.config.GatewayProperties;
import stacs.chessgateway.exceptions.GatewayFailureException;
import stacs.chessgateway.models.MoveMessage;
import stacs.chessgateway.services.GatewayService;

import java.util.Arrays;

@Service
public class GatewayServiceImpl implements GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    private final GatewayProperties gatewayProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public GatewayServiceImpl(GatewayProperties gatewayProperties, ObjectMapper objectMapper) {
        this.gatewayProperties = gatewayProperties;
        this.objectMapper = objectMapper;

        logger.debug(this.gatewayProperties.toString());

        // Initialize the JadeGateway to connect to the running JADE-based system.
        // Assuming the main container is the one we want to use currently
        // to is running in the local host and is using the default port 1099
        Properties pp = new Properties();
        pp.setProperty(Profile.MAIN_HOST, this.gatewayProperties.getMainContainerHostName());
        pp.setProperty(Profile.MAIN_PORT, Integer.toString(this.gatewayProperties.getMainContainerPort()));

        // Override default gateway agent with custom class
        JadeGateway.init(null, pp);
    }


    @Override
    public void sendMove(MoveMessage move) throws GatewayFailureException {
        logger.info("Sending move to agent");
        try {
            JadeGateway.execute(new OneShotBehaviour() {
                @Override
                public void action() {
                    try {
                        final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                        final AID pingAgent = new AID("PingAgent@chess-platform", true);
                        message.addReceiver(pingAgent);
                        message.setConversationId("sending-move");
                        message.setContent(objectMapper.writeValueAsString(move));
                        myAgent.send(message);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (ControllerException | InterruptedException e) {
            throw new GatewayFailureException(Arrays.toString(e.getStackTrace()));
        }
    }
}
