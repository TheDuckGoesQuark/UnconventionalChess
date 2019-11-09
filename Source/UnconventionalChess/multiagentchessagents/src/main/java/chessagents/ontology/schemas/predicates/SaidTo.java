package chessagents.ontology.schemas.predicates;

import jade.content.OntoAID;
import jade.content.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaidTo implements Predicate {

    private OntoAID speaker;
    private String phrase;

}
