package chessagents.agents.pieceagent.goals;

import chessagents.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class ProtectPiecesValue extends Value {
    public ProtectPiecesValue() {
        super("Protect pieces");
    }

    @Override
    public boolean actionMaintainsValue(ChessPiece pieceWithValue, GameState gameState, PieceAction pieceAction) {
        var threatenedPiecesBefore = gameState.getThreatenedPieces();
        var threatenedPiecesAfter = gameState.apply(pieceAction).getThreatenedPieces();
        return threatenedPiecesBefore.size() > threatenedPiecesAfter.size();
    }
}
