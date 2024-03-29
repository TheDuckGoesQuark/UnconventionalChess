package chessagents.agents.pieceagent.personality.values;

import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.argumentation.Opinion;
import chessagents.agents.pieceagent.argumentation.Reasoning;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;

public class MaximiseEnemyThreatenedPieces extends Value {
    public MaximiseEnemyThreatenedPieces() {
        super("Maximise enemy threatened pieces");
    }

    @Override
    public MoveResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        var enemyColour = chessPiece.getColour().flip();
        var enemiesThreatenedBefore = gameState.getThreatenedForColour(enemyColour).size();
        var enemiesThreatenedAfter = gameState.applyMove(action).getThreatenedForColour(enemyColour).size();

        if (enemiesThreatenedBefore < enemiesThreatenedAfter) {
            return MoveResponse.buildResponse(action, Opinion.LIKE, new Reasoning(this, "scare more enemies"));
        } else if (enemiesThreatenedBefore == enemiesThreatenedAfter) {
            return MoveResponse.buildResponse(action, Opinion.NEUTRAL, new Reasoning(this, "not threaten any enemies"));
        } else {
            return MoveResponse.buildResponse(action, Opinion.DISLIKE, new Reasoning(this, "reduce the number of scared enemies"));
        }
    }
}
