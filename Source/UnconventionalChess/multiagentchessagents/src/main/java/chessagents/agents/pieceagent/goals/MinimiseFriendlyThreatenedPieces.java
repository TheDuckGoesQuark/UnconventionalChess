package chessagents.agents.pieceagent.goals;

import chessagents.chess.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class MinimiseFriendlyThreatenedPieces extends Value {
    public MinimiseFriendlyThreatenedPieces() {
        super("Protect pieces");
    }

    @Override
    public boolean actionMaintainsValue(ChessPiece pieceWithValue, GameState gameState, PieceAction pieceAction) {
        var threatenedPiecesBefore = gameState.getThreatenedPieces();
        var threatenedPiecesAfter = gameState.getOutcomeOfAction(pieceAction).getThreatenedPieces();
        return threatenedPiecesBefore.size() > threatenedPiecesAfter.size();
    }
}
