package stacs.chessgateway.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stacs.chessgateway.config.GatewayProperties;
import stacs.chessgateway.services.GatewayService;

@Service
public class GatewayServiceImpl implements GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    private final GatewayProperties gatewayProperties;

    @Autowired
    public GatewayServiceImpl(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;

        logger.debug(this.gatewayProperties.toString());
    }
}
