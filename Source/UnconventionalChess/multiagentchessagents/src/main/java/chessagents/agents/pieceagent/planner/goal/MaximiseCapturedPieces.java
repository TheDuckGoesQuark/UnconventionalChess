package chessagents.agents.pieceagent.planner.goal;

import chessagents.agents.pieceagent.planner.GameState;
import chessagents.agents.pieceagent.planner.PieceAction;

public class MaximiseCapturedPieces extends Value {

    public MaximiseCapturedPieces() {
        super("Maximise captured pieces");
    }

    @Override
    public boolean actionMaintainsValue(GameState gameState, PieceAction pieceAction) {
        return gameState.getEnemiesCaptured().size() < gameState.apply(pieceAction).getEnemiesCaptured().size();
    }
}
