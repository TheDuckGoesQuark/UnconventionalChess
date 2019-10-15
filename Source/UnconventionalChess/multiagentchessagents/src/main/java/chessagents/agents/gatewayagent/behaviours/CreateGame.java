package chessagents.agents.gatewayagent.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import chessagents.agents.gameagent.GameAgentProperties;

public class CreateGame extends OneShotBehaviour {

    private static final String GAME_AGENT_CLASS = "chessagents.agents.gameagent.GameAgent";

    private final GameAgentProperties properties;
    private final String gameAgentId;

    public CreateGame(GameAgentProperties properties, String gameAgentId) {
        this.properties = properties;
        this.gameAgentId = gameAgentId;
    }

    private AgentController createGameAgent(GameAgentProperties properties) throws StaleProxyException {
        final ContainerController container = myAgent.getContainerController();

        return container.createNewAgent(
                gameAgentId,
                GAME_AGENT_CLASS,
                new Object[]{properties, myAgent.getName()}
        );
    }

    @Override
    public void action() {
        try {
            final AgentController agentController = createGameAgent(properties);
            agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
