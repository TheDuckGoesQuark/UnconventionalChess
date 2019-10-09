package chessagents.ontology.schemas.concepts;

import jade.content.Concept;

public class Position implements Concept {

    private String coordinates;

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
