package chessagents.ontology.schemas.concepts;

import com.github.bhlangonijr.chesslib.Side;
import jade.content.Concept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Colour implements Concept {

    public static final String WHITE = Side.WHITE.value();
    public static final String BLACK = Side.BLACK.value();

    private String colour;
}
