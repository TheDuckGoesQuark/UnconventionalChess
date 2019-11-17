package chessagents.ontology.schemas.concepts;

import jade.content.Concept;
import jade.content.OntoAID;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChessPiece implements Concept {

    private OntoAID agentAID;
    private Colour colour;
    private String type;
    private Position position;

    /**
     * Constructor for when AID is not known initially
     *
     * @param colour   colour of piece
     * @param type     type of piece
     * @param position current position of piece
     */
    public ChessPiece(Colour colour, String type, Position position) {
        this.colour = colour;
        this.type = type;
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return colour.equals(that.colour) &&
                type.equals(that.type) &&
                position.equals(that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colour, type, position);
    }
}
