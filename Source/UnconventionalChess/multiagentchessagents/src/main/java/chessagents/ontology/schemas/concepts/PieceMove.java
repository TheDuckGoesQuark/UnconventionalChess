package chessagents.ontology.schemas.concepts;

import jade.content.Concept;

import java.util.Objects;

public class PieceMove implements Concept {

    private Position source;
    private Position target;

    public PieceMove() {
    }

    public PieceMove(String source, String target) {
        this.source = new Position();
        this.source.setCoordinates(source);
        this.target = new Position();
        this.target.setCoordinates(target);
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceMove pieceMove = (PieceMove) o;
        return source.equals(pieceMove.source) &&
                target.equals(pieceMove.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
