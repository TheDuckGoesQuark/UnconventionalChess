package chessagents.agents.pieceagent;

import chessagents.agents.pieceagent.actions.PieceAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;

@Getter
@AllArgsConstructor
public class ActionResponse {
    private final PieceAction action;
    private final boolean approveAction;
    private final SPhraseSpec reasoning;
}
