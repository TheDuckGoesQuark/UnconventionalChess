package chessagents.ontology.schemas.predicates;

import chessagents.ontology.schemas.concepts.Move;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IsValidMove {

    private Move move;

}
