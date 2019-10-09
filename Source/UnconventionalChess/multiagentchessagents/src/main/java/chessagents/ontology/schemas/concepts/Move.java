package chessagents.ontology.schemas.concepts;

import jade.content.Concept;

public class Move implements Concept {

    private Position source;
    private Position target;

    public Position getSource() {
        return source;
    }

    public void setSource(Position source) {
        this.source = source;
    }

    public Position getTarget() {
        return target;
    }

    public void setTarget(Position target) {
        this.target = target;
    }
}
