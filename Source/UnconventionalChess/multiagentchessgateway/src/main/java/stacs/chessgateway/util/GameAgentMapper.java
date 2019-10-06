package stacs.chessgateway.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GameAgentMapper {

    private final Map<Integer, String> gameIdToAgent = new HashMap<>();
    private final Map<String, Integer> agentToGameId = new HashMap<>();

    public GameAgentMapper() {
    }

    public void addMapping(int gameId, String agentId) {
        gameIdToAgent.put(gameId, agentId);
        agentToGameId.put(agentId, gameId);
    }

    public void removeGameById(int gameId) {
        String agent = gameIdToAgent.remove(gameId);
        agentToGameId.remove(agent);
    }

    public void removeGameByAgent(String agent) {
        int gameId = agentToGameId.remove(agent);
        gameIdToAgent.remove(gameId);
    }

    public int getGameIdByAgent(String agent) {
        return agentToGameId.get(agent);
    }

    public String getAgentByGameId(int gameId) {
        return gameIdToAgent.get(gameId);
    }

    public int size() {
        return gameIdToAgent.size();
    }
}
