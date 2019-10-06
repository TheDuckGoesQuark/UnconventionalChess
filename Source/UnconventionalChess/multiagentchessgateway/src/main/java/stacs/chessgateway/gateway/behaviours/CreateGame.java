package stacs.chessgateway.gateway.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import chessagents.gameagent.GameAgentProperties;
import stacs.chessgateway.models.GameConfiguration;

public class CreateGame extends OneShotBehaviour {

    private static final String GAME_AGENT_CLASS = "chessagents.gameagent.GameAgent";
    private final GameConfiguration gameConfiguration;
    private final String gameAgentId;

    public CreateGame(GameConfiguration gameConfiguration, String gameAgentId) {
        this.gameConfiguration = gameConfiguration;
        this.gameAgentId = gameAgentId;
    }

    private GameAgentProperties createProperties() {
        return new GameAgentProperties(
                gameConfiguration.isHumanPlays(),
                gameConfiguration.isHumanPlaysAsWhite()
        );
    }

    private AgentController createGameAgent(GameAgentProperties properties) throws StaleProxyException {
        final ContainerController container = myAgent.getContainerController();

        return container.createNewAgent(
                GAME_AGENT_CLASS,
                gameAgentId,
                new Object[]{properties}
        );
    }

    @Override
    public void action() {
        final GameAgentProperties properties = createProperties();

        try {
            final AgentController agentController = createGameAgent(properties);
            agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
