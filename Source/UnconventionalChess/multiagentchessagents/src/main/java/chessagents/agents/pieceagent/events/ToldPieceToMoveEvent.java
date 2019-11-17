package chessagents.agents.pieceagent.events;

import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import lombok.Getter;

@Getter
public class ToldPieceToMoveEvent extends TransitionEvent {
    private final ChessPiece toldChessPiece;
    private final PieceMove move;

    public ToldPieceToMoveEvent(ChessPiece toldChessPiece, PieceMove move) {
        super(PieceTransition.TOLD_PIECE_TO_MOVE);
        this.toldChessPiece = toldChessPiece;
        this.move = move;
    }
}
