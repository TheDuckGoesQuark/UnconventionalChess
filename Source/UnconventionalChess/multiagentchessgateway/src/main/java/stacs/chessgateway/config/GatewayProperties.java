package stacs.chessgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jade.gateway")
public class GatewayProperties {

    private String mainContainerHostName;
    private int mainContainerPort;

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

    @Override
    public String toString() {
        return "GatewayProperties{" +
                "mainContainerHostName='" + mainContainerHostName + '\'' +
                ", mainContainerPort=" + mainContainerPort +
                '}';
    }
}
