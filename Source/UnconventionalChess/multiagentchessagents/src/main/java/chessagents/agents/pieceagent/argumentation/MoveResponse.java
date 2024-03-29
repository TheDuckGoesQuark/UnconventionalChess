package chessagents.agents.pieceagent.argumentation;

import chessagents.ontology.schemas.concepts.PieceMove;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class MoveResponse implements Serializable {
    private final PieceMove move;
    private final Opinion opinion;
    private final Reasoning reasoning;
    private MoveResponse alternativeResponse = null;
    private boolean performed = false;

    public static MoveResponse buildResponse(PieceMove move, Opinion opinion, Reasoning reasoning) {
        return new MoveResponse(move, opinion, reasoning);
    }

    private MoveResponse(PieceMove move, Opinion opinion, Reasoning opinionGeneratingValue) {
        this.move = move;
        this.opinion = opinion;
        this.reasoning = opinionGeneratingValue;
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

    protected MoveResponse clone() {
        return new MoveResponse(move, opinion, reasoning, alternativeResponse, performed);
    }
}
