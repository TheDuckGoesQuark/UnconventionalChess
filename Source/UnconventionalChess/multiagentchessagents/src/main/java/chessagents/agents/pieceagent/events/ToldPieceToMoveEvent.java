package chessagents.agents.pieceagent.events;

import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.Move;
import chessagents.ontology.schemas.concepts.Piece;
import lombok.Getter;

@Getter
public class ToldPieceToMoveEvent extends Event {
    private final Piece toldPiece;
    private final Move move;

    public ToldPieceToMoveEvent(Piece toldPiece, Move move) {
        super(PieceTransition.TOLD_PIECE_TO_MOVE);
        this.toldPiece = toldPiece;
        this.move = move;
    }
}
