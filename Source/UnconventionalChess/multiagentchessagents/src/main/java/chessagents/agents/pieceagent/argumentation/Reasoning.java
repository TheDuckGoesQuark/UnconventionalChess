package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.pieceagent.personality.values.Value;
import lombok.Getter;

@Getter
public class Reasoning {
    private final String justification;
    private final Value valueResponsible;

    public Reasoning(String justification, Value valueResponsible) {
        this.justification = justification;
        this.valueResponsible = valueResponsible;
    }
}
