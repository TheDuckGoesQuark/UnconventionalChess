package chessagents.agents.gatewayagent.behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import chessagents.agents.gameagent.GameProperties;

/**
 * Spawns a new game agent with the requested game id
 */
public class CreateGameAgent extends OneShotBehaviour {

    private static final String GAME_AGENT_CLASS = "chessagents.agents.gameagent.GameAgent";

    private final GameProperties properties;
    private final AID gameAgentId;

    public CreateGameAgent(GameProperties properties, AID gameAgentId) {
        this.properties = properties;
        this.gameAgentId = gameAgentId;
    }

    private AgentController createGameAgent(GameProperties properties) throws StaleProxyException {
        final ContainerController container = myAgent.getContainerController();

        return container.createNewAgent(
                gameAgentId.getLocalName(),
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
