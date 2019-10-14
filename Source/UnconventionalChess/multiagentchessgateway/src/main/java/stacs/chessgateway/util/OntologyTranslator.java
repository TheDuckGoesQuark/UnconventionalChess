package stacs.chessgateway.util;

import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Move;
import org.springframework.stereotype.Component;
import stacs.chessgateway.models.MoveMessage;

@Component
public class OntologyTranslator {

    public MakeMove translateToOntology(MoveMessage move) {
        final MakeMove makeMove = new MakeMove();
        makeMove.setMove(new Move(move.getSourceSquare(), move.getTargetSquare()));
        return makeMove;
    }
}
