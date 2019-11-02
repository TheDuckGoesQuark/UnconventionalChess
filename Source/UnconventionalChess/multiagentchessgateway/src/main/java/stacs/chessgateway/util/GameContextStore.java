package stacs.chessgateway.util;

import chessagents.agents.pieceagent.PieceContext;
import jade.core.AID;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class GameContextStore {

    private final Map<Integer, AID> gameIdToAgent = new HashMap<>();
    private final Map<AID, Integer> agentToGameId = new HashMap<>();

    public GameContextStore() {
    }

    public void addMapping(int gameId, AID agentId) {
        gameIdToAgent.put(gameId, agentId);
        agentToGameId.put(agentId, gameId);
    }

    public void removeGameById(int gameId) {
        var agent = gameIdToAgent.remove(gameId);
        agentToGameId.remove(agent);
    }

    public void removeGameByAgent(AID agent) {
        int gameId = agentToGameId.remove(agent);
        gameIdToAgent.remove(gameId);
    }

    public int getGameIdByAgent(AID agent) {
        return agentToGameId.get(agent);
    }

    public AID getAgentByGameId(int gameId) {
        return gameIdToAgent.get(gameId);
    }

    public int size() {
        return gameIdToAgent.size();
    }
}
