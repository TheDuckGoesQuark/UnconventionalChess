package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.pieceagent.personality.values.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class Reasoning implements Serializable {

    private final Value value;
    private final String justification;

}
