package chessagents.ontology.schemas.predicates;

import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.content.Predicate;

public class CanMakeMove implements Predicate {

    private ChessPiece chessPiece;
    private PieceMove move;

    public ChessPiece getChessPiece() {
        return chessPiece;
    }

    public void setChessPiece(ChessPiece chessPiece) {
        this.chessPiece = chessPiece;
    }

    public PieceMove getMove() {
        return move;
    }

    public void setMove(PieceMove move) {
        this.move = move;
    }
}
