package chessagents.agents.pieceagent.personality.values;

import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.argumentation.Opinion;
import chessagents.agents.pieceagent.argumentation.Reasoning;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;

public class MaximiseMyMoves extends Value {
    public MaximiseMyMoves() {
        super("Maximise my Moves");
    }

    @Override
    public MoveResponse getMoveResponse(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        var myPosition = chessPiece.getPosition();

        if (action.getSource().equals(myPosition)) {
            return MoveResponse.buildResponse(action, Opinion.LIKE, new Reasoning(this, "involve me"));
        } else {
            return MoveResponse.buildResponse(action, Opinion.DISLIKE, new Reasoning(this, "doesn't involve me"));
        }
    }
}
