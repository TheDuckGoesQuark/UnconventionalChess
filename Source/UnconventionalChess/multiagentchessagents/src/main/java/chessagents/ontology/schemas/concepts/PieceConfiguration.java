package chessagents.ontology.schemas.concepts;

import jade.content.Concept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PieceConfiguration implements Concept {

    private String startingPosition;
    private String name;
    private String personality;

}
