package chessagents.ontology.schemas.concepts;

import com.github.bhlangonijr.chesslib.Side;
import jade.content.Concept;

public class Colour implements Concept {

    public static final String WHITE = Side.WHITE.value();
    public static final String BLACK = Side.BLACK.value();

    private String colour;

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
