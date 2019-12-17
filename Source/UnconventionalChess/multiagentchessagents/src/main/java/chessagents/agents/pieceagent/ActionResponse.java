package chessagents.agents.pieceagent;

import chessagents.agents.pieceagent.actions.PieceAction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActionResponse {
    private final PieceAction action;
    private final boolean approveAction;
}
