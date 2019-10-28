package stacs.chessgateway.gateway;

import jade.core.Profile;
import jade.util.leap.Properties;
import jade.wrapper.gateway.JadeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stacs.chessgateway.config.GatewayProperties;

@Component
public class GatewayInitiator {

    private static final Logger logger = LoggerFactory.getLogger(GatewayInitiator.class);
    private static final String GATEWAY_AGENT_CLASS_NAME = "chessagents.agents.gatewayagent.ChessGatewayAgent";

    @Autowired
    public GatewayInitiator(GatewayProperties gatewayProperties) {
        // Initialize the JadeGateway to connect to the running JADE-based system.
        // Assuming the main container is the one we want to use
        Properties pp = new Properties();
        pp.setProperty(Profile.MAIN_HOST, gatewayProperties.getMainContainerHostName());
        pp.setProperty(Profile.MAIN_PORT, Integer.toString(gatewayProperties.getMainContainerPort()));

        // Override default gateway agent with custom class
        JadeGateway.init(GATEWAY_AGENT_CLASS_NAME, pp);

        logger.info("JadeGateway initialised.");
    }
}
