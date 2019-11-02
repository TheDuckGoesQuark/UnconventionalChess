package chessagents.ontology.schemas.predicates;


import jade.content.OntoAID;
import jade.content.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IsSpeaker implements Predicate {

    private OntoAID agent;

}
