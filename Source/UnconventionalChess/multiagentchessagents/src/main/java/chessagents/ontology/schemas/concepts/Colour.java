package chessagents.ontology.schemas.concepts;

import com.github.bhlangonijr.chesslib.Side;
import jade.content.Concept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Colour implements Concept {

    public static final Colour WHITE = new Colour(Side.WHITE.value());
    public static final Colour BLACK = new Colour(Side.BLACK.value());

    private String colour;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Colour colour1 = (Colour) o;
        return Objects.equals(colour, colour1.colour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colour);
    }
}
