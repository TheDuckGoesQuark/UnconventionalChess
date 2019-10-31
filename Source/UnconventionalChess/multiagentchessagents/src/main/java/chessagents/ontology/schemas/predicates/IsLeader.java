package chessagents.ontology.schemas.predicates;


import jade.content.OntoAID;
import jade.content.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IsLeader implements Predicate {

    private OntoAID agent;

}
