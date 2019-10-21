package stacs.chessgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jade.gateway")
public class GatewayProperties {

    private String mainContainerHostName;
    private int mainContainerPort;
    private String platformName;

    public String getMainContainerHostName() {
        return mainContainerHostName;
    }

    public void setMainContainerHostName(String mainContainerHostName) {
        this.mainContainerHostName = mainContainerHostName;
    }

    public int getMainContainerPort() {
        return mainContainerPort;
    }

    public void setMainContainerPort(int mainContainerPort) {
        this.mainContainerPort = mainContainerPort;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
