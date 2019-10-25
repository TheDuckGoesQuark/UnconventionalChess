package chessagents.ontology.schemas.concepts;

import jade.content.Concept;
import jade.content.OntoAID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Piece implements Concept {

    private OntoAID agentAID;
    private Colour colour;
    private String type;
    private Position position;

}
