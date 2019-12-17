package chessagents.agents.pieceagent;

import chessagents.ontology.schemas.concepts.PieceMove;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActionResponse {
    private final PieceMove action;
    private final boolean approveAction;
}
