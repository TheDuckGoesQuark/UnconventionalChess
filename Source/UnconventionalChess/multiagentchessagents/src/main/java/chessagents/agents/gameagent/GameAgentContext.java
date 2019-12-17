package chessagents.agents.gameagent;

import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameAgentContext {

    private final AID gatewayAgentAID;
    private final GameProperties gameProperties;
    private GameCreationStatus gameCreationStatus = GameCreationStatus.NOT_EXIST;
    private GameState gameState;
    private int moveCounter = 0;

    public GameAgentContext(AID gatewayAgentAID, GameProperties gameProperties) {
        this.gatewayAgentAID = gatewayAgentAID;
        this.gameProperties = gameProperties;
    }

    public void makeMove(PieceMove move) {
        gameState = gameState.applyMove(move);
        moveCounter++;
    }

    public boolean isFirstTurnForAgentSide() {
        if (gameProperties.isHumanPlays()) {
            int firstAgentTurnIndex = gameProperties.isHumanPlaysAsWhite() ? 1 : 0;
            return firstAgentTurnIndex == moveCounter;
        } else {
            // if both sides are agent controlled then both sides will need initialised speakers
            return moveCounter <= 1;
        }
    }
}
