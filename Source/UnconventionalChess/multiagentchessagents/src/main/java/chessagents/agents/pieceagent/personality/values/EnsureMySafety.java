package chessagents.agents.pieceagent.personality.values;

import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.argumentation.Opinion;
import chessagents.agents.pieceagent.argumentation.Reasoning;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;


public class EnsureMySafety extends Value {

    public EnsureMySafety() {
        super("Ensure my safety");
    }

    @Override
    public MoveResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        // choose action that produces state where I am not captured and not in the set of captured pieces
        var afterActionState = gameState.applyMove(action);
        var isAlreadyThreatened = gameState.getThreatenedPieces().contains(chessPiece);
        var becomesThreatened = afterActionState.getThreatenedPieces().contains(chessPiece);
        var isCaptured = afterActionState.getCapturedForColour(chessPiece.getColour()).contains(chessPiece);

        if (isAlreadyThreatened && !becomesThreatened && !isCaptured) {
            return MoveResponse.buildResponse(action, Opinion.LIKE, new Reasoning(this, "save me from being threatened"));
        } else if (becomesThreatened || isCaptured) {
            return MoveResponse.buildResponse(action, Opinion.DISLIKE, new Reasoning(this, "put me at risk"));
        } else {
            return MoveResponse.buildResponse(action, Opinion.NEUTRAL, new Reasoning(this, "not affect me"));
        }
    }
}
