package chessagents.ontology.schemas.predicates;


import chessagents.ontology.schemas.concepts.PieceMove;
import jade.content.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoveMade implements Predicate {

    private PieceMove move;

}
