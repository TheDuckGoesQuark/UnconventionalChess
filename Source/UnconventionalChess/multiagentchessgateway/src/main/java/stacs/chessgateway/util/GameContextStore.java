package stacs.chessgateway.util;

import chessagents.agents.pieceagent.PieceContext;
import jade.core.AID;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class GameContextStore {

    private final Map<Integer, String> gameIdToMoveSubscription = new HashMap<>();
    private final Map<Integer, AID> gameIdToAgent = new HashMap<>();
    private final Map<AID, Integer> agentToGameId = new HashMap<>();

    public GameContextStore() {
    }

    public void addMapping(int gameId, AID agentId, String moveSubscription) {
        gameIdToAgent.put(gameId, agentId);
        agentToGameId.put(agentId, gameId);
        gameIdToMoveSubscription.put(gameId, moveSubscription);
    }

    public void removeGameById(int gameId) {
        var agent = gameIdToAgent.remove(gameId);
        agentToGameId.remove(agent);
        gameIdToMoveSubscription.remove(gameId);
    }

    public void removeGameByAgent(AID agent) {
        int gameId = agentToGameId.remove(agent);
        gameIdToAgent.remove(gameId);
        gameIdToMoveSubscription.remove(gameId);
    }

    public int getGameIdByAgent(AID agent) {
        return agentToGameId.get(agent);
    }

    public AID getAgentByGameId(int gameId) {
        return gameIdToAgent.get(gameId);
    }

    public String moveSubscriptionIDByGameID(int gameId) {
        return gameIdToMoveSubscription.get(gameId);
    }

    public String gameIdByMoveSubscriptionID(String moveSubscriptionID) {
        return gameIdToMoveSubscription.values().stream()
                .filter(id -> id.equals(moveSubscriptionID))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(moveSubscriptionID));
    }

    public int size() {
        return gameIdToAgent.size();
    }
}
