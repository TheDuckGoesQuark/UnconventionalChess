package chessagents.agents.gameagent;

import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Piece;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class GameContext {

    private final AID gatewayAgentAID;
    private final GameProperties gameProperties;
    private final BoardWrapper board = new BoardWrapper();
    private final Map<AID, Piece> piecesByAID = new HashMap<>();

    private GameStatus gameStatus = GameStatus.NOT_EXIST;
    private int gameId;

    public GameContext(AID gatewayAgentAID, GameProperties gameProperties) {
        this.gatewayAgentAID = gatewayAgentAID;
        this.gameProperties = gameProperties;
    }

}
