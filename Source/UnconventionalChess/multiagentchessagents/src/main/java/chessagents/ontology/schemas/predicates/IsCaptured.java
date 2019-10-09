package chessagents.ontology.schemas.predicates;

import chessagents.ontology.schemas.concepts.Piece;
import jade.content.Predicate;

public class IsCaptured implements Predicate {

    private Piece piece;

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
