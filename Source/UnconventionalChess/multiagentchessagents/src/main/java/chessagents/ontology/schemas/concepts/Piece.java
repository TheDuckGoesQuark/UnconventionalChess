package chessagents.ontology.schemas.concepts;

import jade.content.Concept;
import jade.content.OntoAID;

public class Piece implements Concept {

    private OntoAID pieceAgent;
    private Colour colour;
    private String type;

    public OntoAID getPieceAgent() {
        return pieceAgent;
    }

    public void setPieceAgent(OntoAID pieceAgent) {
        this.pieceAgent = pieceAgent;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
