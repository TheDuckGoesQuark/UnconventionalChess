package chessagents.agents.gatewayagent;

import jade.core.AID;

public interface MessageHandler<M> {

    /**
     * Handles message that originated from an agent
     *
     * @param message message received from an agent
     * @param agentID id of agent that sent message
     */
    void handleAgentMessage(M message, AID agentID);

}
