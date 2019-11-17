package chessagents.ontology.schemas.predicates;

import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.content.Predicate;

public class IsCaptured implements Predicate {

    private ChessPiece chessPiece;

    public ChessPiece getChessPiece() {
        return chessPiece;
    }

    public void setChessPiece(ChessPiece chessPiece) {
        this.chessPiece = chessPiece;
    }
}
