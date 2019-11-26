package chessagents.agents.gameagent;

import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.PieceMove;
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
    private GameCreationStatus gameCreationStatus = GameCreationStatus.NOT_EXIST;
    private GameState gameState;

    public GameAgentContext(AID gatewayAgentAID, GameProperties gameProperties) {
        this.gatewayAgentAID = gatewayAgentAID;
        this.gameProperties = gameProperties;
    }

    public boolean isHumanTurn() {
        return gameProperties.isHumanPlays() && (gameProperties.isHumanPlaysAsWhite() ? gameState.isSideToGo(WHITE) : gameState.isSideToGo(BLACK));
    }

    public void makeMove(PieceMove move) {
        gameState = gameState.makeMove(move);
    }
}
