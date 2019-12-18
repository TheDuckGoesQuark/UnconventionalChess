package chessagents.agents.pieceagent.personality.values;

import chessagents.agents.pieceagent.ActionResponse;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;


public class EnsureMySafety extends Value {

    public EnsureMySafety() {
        super("Ensure my safety");
    }

    public boolean actionMaintainsValue(ChessPiece pieceWithValue, GameState gameState, PieceMove pieceAction) {
        // choose action that produces state where I am not captured and not in the set of captured pieces
        var afterActionState = gameState.applyMove(pieceAction);

        // TODO contains check will fail if I moved/was captured as a clone is made with a different position
        // and current piece hashcode includes the position in the equals check
        // might need to apply some sort of ID to all the pieces, agent or not
        return !afterActionState.getThreatenedPieces().contains(pieceWithValue)
                && afterActionState.getCapturedForColour(pieceWithValue.getColour()).contains(pieceWithValue);
    }

    @Override
    public ActionResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        var approves = actionMaintainsValue(chessPiece, gameState, action);
        return new ActionResponse(action, approves);
    }
}
