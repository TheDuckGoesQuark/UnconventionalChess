package chessagents.agents.pieceagent.argumentation;

import chessagents.ontology.schemas.concepts.PieceMove;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class MoveResponse {
    private final PieceMove move;
    private final Opinion opinion;
    private final Reasoning reasoning;
    private MoveResponse alternativeResponse = null;
    private boolean performed = false;

    public MoveResponse(PieceMove move, Opinion opinion, Reasoning reasoning) {
        this.move = move;
        this.opinion = opinion;
        this.reasoning = reasoning;
    }

    public boolean performed() {
        return performed;
    }

    public Optional<MoveResponse> getAlternativeResponse() {
        return Optional.ofNullable(alternativeResponse);
    }

    public Optional<PieceMove> getMove() {
        return Optional.ofNullable(move);
    }
}
