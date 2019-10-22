package chessagents.ontology.schemas.concepts;

import jade.content.Concept;
import jade.content.OntoAID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Piece implements Concept {

    private OntoAID pieceAgent;
    private Colour colour;
    private String type;
    private Position position;

}
