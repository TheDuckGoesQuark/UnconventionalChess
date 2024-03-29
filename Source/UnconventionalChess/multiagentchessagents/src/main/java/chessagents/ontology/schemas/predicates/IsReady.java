package chessagents.ontology.schemas.predicates;

import chessagents.ontology.schemas.concepts.Game;
import jade.content.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IsReady implements Predicate {

    private Game game;

}
