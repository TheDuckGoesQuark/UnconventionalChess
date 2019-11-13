package chessagents.agents.gameagent;

import chessagents.GameContext;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import static chessagents.ontology.schemas.concepts.Colour.BLACK;
import static chessagents.ontology.schemas.concepts.Colour.WHITE;

@Getter
@Setter
public class GameAgentContext {

    private final AID gatewayAgentAID;
    private final GameProperties gameProperties;
    private final GameContext gameContext = new GameContext();
    private GameCreationStatus gameCreationStatus = GameCreationStatus.NOT_EXIST;

    public GameAgentContext(AID gatewayAgentAID, GameProperties gameProperties) {
        this.gatewayAgentAID = gatewayAgentAID;
        this.gameProperties = gameProperties;
    }

    public boolean isHumanTurn() {
        var board = gameContext.getBoard();
        var gameProperties = getGameProperties();

        return gameProperties.isHumanPlays() &&
                (gameProperties.isHumanPlaysAsWhite() ? board.isSideToGo(WHITE) : board.isSideToGo(BLACK));
    }
}
