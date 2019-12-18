package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.pieceagent.personality.values.Value;
import chessagents.ontology.schemas.concepts.PieceMove;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
public class MoveResponse implements Serializable {
    private final PieceMove move;
    private final Opinion opinion;
    private final Value opinionGeneratingValue;
    private MoveResponse alternativeResponse = null;
    private boolean performed = false;

    public static MoveResponse buildResponse(PieceMove move, Opinion opinion, Value reasoning) {
        return new MoveResponse(move, opinion, reasoning);
    }

    private MoveResponse(PieceMove move, Opinion opinion, Value opinionGeneratingValue) {
        this.move = move;
        this.opinion = opinion;
        this.opinionGeneratingValue = opinionGeneratingValue;
    }

    private MoveResponse(PieceMove move, Opinion opinion, Value opinionGeneratingValue, MoveResponse alternativeResponse, boolean performed) {
        this.move = move;
        this.opinion = opinion;
        this.opinionGeneratingValue = opinionGeneratingValue;
        this.alternativeResponse = alternativeResponse;
        this.performed = performed;
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
        return new MoveResponse(move, opinion, opinionGeneratingValue, alternativeResponse, performed);
    }
}
