package chessagents.agents.gameagent;

import chessagents.agents.analysis.GameLogger;
import chessagents.agents.analysis.GameLoggerImpl;
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
    private final GameLogger gameLogger;
    private GameCreationStatus gameCreationStatus = GameCreationStatus.NOT_EXIST;
    private GameState gameState;
    private int moveCounter = 0;

    public GameAgentContext(AID gatewayAgentAID, GameProperties gameProperties) {
        this.gatewayAgentAID = gatewayAgentAID;
        this.gameProperties = gameProperties;
        this.gameLogger = new GameLoggerImpl();
    }

    public void makeMove(PieceMove move) {
        if (!isHumanTurn()) {
            gameLogger.logMoveAndState(
                    move.getSource().getCoordinates(),
                    move.getTarget().getCoordinates(),
                    gameState.getFen()
            );
        }
        gameState = gameState.applyMove(move);
        moveCounter++;
    }

    private boolean isHumanTurn() {
        return gameProperties.isHumanPlays()
                && (gameProperties.isHumanPlaysAsWhite() == (moveCounter % 2 == 0));
    }
}
