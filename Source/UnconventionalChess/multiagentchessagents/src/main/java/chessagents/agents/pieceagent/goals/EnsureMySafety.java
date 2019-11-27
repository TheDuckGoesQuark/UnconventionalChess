package chessagents.agents.pieceagent.goals;

import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class EnsureMySafety extends Value {

    public EnsureMySafety() {
        super("Ensure my safety");
    }

    @Override
    public boolean actionMaintainsValue(ChessPiece pieceWithValue, GameState gameState, PieceAction pieceAction) {
        // choose action that produces state where I am not captured and not in the set of captured pieces
        var afterActionState = gameState.getOutcomeOfAction(pieceAction);

        // TODO contains check will fail if I moved/was captured as a clone is made with a different position
        // and current piece hashcode includes the position in the equals check
        // might need to apply some sort of ID to all the pieces, agent or not
        return !afterActionState.getThreatenedPieces().contains(pieceWithValue)
                && afterActionState.getCapturedForColour(pieceWithValue.getColour()).contains(pieceWithValue);
    }
}
