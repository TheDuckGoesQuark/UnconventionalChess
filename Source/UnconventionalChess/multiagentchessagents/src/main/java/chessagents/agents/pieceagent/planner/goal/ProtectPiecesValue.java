package chessagents.agents.pieceagent.planner.goal;

import chessagents.agents.pieceagent.planner.GameState;
import chessagents.agents.pieceagent.planner.PieceAction;

public class ProtectPiecesValue extends Value {
    public ProtectPiecesValue() {
        super("Protect pieces");
    }

    @Override
    public boolean actionMaintainsValue(GameState gameState, PieceAction pieceAction) {
        return gameState.getFriendlyThreatenedPieces().size() < gameState.apply(pieceAction).getFriendlyThreatenedPieces().size();
    }
}
