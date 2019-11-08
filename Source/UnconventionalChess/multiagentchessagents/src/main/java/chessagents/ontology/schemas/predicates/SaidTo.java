package chessagents.ontology.schemas.predicates;

import jade.content.AgentAction;
import jade.content.OntoAID;
import jade.content.Predicate;
import jade.content.schema.AgentActionSchema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaidTo implements Predicate  {

    private OntoAID speaker;
    private OntoAID listener;
    private String phrase;

}
