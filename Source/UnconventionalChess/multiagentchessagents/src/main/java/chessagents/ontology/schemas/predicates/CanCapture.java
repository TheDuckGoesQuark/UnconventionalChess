package chessagents.ontology.schemas.predicates;


import chessagents.ontology.schemas.concepts.Piece;
import jade.content.Predicate;

public class CanCapture implements Predicate {

    private Piece attacker;
    private Piece victim;

    public Piece getAttacker() {
        return attacker;
    }

    public void setAttacker(Piece attacker) {
        this.attacker = attacker;
    }

    public Piece getVictim() {
        return victim;
    }

    public void setVictim(Piece victim) {
        this.victim = victim;
    }
}
