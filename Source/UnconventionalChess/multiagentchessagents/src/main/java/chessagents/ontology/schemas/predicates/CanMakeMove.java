package chessagents.ontology.schemas.predicates;

import chessagents.ontology.schemas.concepts.Move;
import chessagents.ontology.schemas.concepts.Piece;
import jade.content.Predicate;

public class CanMakeMove implements Predicate {

    private Piece piece;
    private Move move;

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }
}
