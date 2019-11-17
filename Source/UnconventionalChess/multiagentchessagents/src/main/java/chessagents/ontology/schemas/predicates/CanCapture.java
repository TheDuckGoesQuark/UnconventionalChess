package chessagents.ontology.schemas.predicates;


import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.content.Predicate;

public class CanCapture implements Predicate {

    private ChessPiece attacker;
    private ChessPiece victim;

    public ChessPiece getAttacker() {
        return attacker;
    }

    public void setAttacker(ChessPiece attacker) {
        this.attacker = attacker;
    }

    public ChessPiece getVictim() {
        return victim;
    }

    public void setVictim(ChessPiece victim) {
        this.victim = victim;
    }
}
