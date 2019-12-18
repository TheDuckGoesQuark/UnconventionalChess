package chessagents.agents.pieceagent.personality;

import chessagents.agents.pieceagent.personality.values.*;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public enum Trait {
    AGGRESSIVE(new Value[]{new MaximiseEnemyCapturedPieces()}),
    DEFENSIVE(new Value[]{new MinimiseFriendlyThreatenedPieces()}),
    EGOTISTIC(new Value[]{new EnsureMySafety()});
    //    BENEVOLENT(new Value[]{}),

    private final Set<Value> appealingValues;

    Trait(Value[] values) {
        appealingValues = new HashSet<>(Arrays.asList(values));
    }
}

