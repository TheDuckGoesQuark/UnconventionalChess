package chessagents.agents.pieceagent.actions;

import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class TryTellPieceToMoveAction extends PieceAction {
    public TryTellPieceToMoveAction(ChessPiece me) {
        super(PieceTransition.NOT_REQUESTING_PROPOSALS, "Try tell piece to move", me);
    }
}
