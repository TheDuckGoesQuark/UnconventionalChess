package stacs.chessgateway.services.impl;

import jade.core.AID;
import jade.core.Profile;
import jade.util.leap.List;
import jade.util.leap.Properties;
import jade.wrapper.gateway.JadeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stacs.chessgateway.config.GatewayProperties;
import stacs.chessgateway.services.GatewayService;
import stacs.chessgateway.util.MainContainerAgentsRetriever;

@Service
public class GatewayServiceImpl implements GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    private final GatewayProperties gatewayProperties;

    @Autowired
    public GatewayServiceImpl(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;

        logger.debug(this.gatewayProperties.toString());

        // Initialize the JadeGateway to connect to the running JADE-based system.
        // In this example we assume the Main Container of the JADE-based system we want to connect
        // to is running in the local host and is using the default port 1099
        Properties pp = new Properties();
        pp.setProperty(Profile.MAIN_HOST, this.gatewayProperties.getMainContainerHostName());
        pp.setProperty(Profile.MAIN_PORT, Integer.toString(this.gatewayProperties.getMainContainerPort()));
        JadeGateway.init(null, pp);

        // Now retrieve all agents active in the Main Container by running (through the JadeGateway)
        // a behaviour that requests that information to the AMS. This behaviour will be executed
        // by the Gateway Agent inside the JadeGateway. As soon as the execution completes the execute() method
        // will return.
        try {
            MainContainerAgentsRetriever retriever = new MainContainerAgentsRetriever();
            JadeGateway.execute(retriever);
            // At this point the retriever behaviour has been fully executed --> the list of
            // agents running in the Main Container is available: get it and print it
            List agents = retriever.getAgents();
            if (agents != null) {
                System.out.println("Agents living in the Main Container: ");
                for (int i = 0; i < agents.size(); ++i) {
                    System.out.println("- " + ((AID) agents.get(i)).getLocalName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
