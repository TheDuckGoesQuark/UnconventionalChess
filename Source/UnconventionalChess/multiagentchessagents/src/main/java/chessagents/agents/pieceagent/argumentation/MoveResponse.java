package chessagents.agents.pieceagent.argumentation;

import chessagents.ontology.schemas.concepts.PieceMove;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveResponse {
    private final PieceMove move;
    private final Opinion opinion;
    private final Reasoning reasoning;
    private MoveResponse alternativeResponse;
    private boolean performed;

    public MoveResponse(PieceMove move, Opinion opinion, Reasoning reasoning) {
        this.move = move;
        this.opinion = opinion;
        this.reasoning = reasoning;
    }

    public boolean performed() {
        return performed;
    }
}
