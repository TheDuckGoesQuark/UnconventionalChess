package chessagents.agents.pieceagent.personality;

import chessagents.agents.pieceagent.personality.values.*;
import lombok.Getter;
import rita.RiGrammar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public enum Trait {
    AGGRESSIVE(new Value[]{new MaximiseEnemyCapturedPieces(), new MaximiseEnemyThreatenedPieces()}),
    PROTECTIVE(new Value[]{new MinimiseFriendlyThreatenedPieces(), new KeepNearOtherPieces()}),
    EGOTISTIC(new Value[]{new EnsureMySafety(), new MaximiseMyMoves()}),
    INTROVERT(new Value[]{new KeepAwayFromOtherPieces(), new MinimiseFriendlyThreatenedPieces()}),
    EXTROVERT(new Value[]{new KeepNearOtherPieces()}),
    BULLY(new Value[]{new EnsureMySafety(), new MaximiseEnemyThreatenedPieces()});

    private final Set<Value> appealingValues;
    private RiGrammar riGrammar;

    Trait(Value[] values) {
        appealingValues = new HashSet<>(Arrays.asList(values));

        try {
            riGrammar = new RiGrammar().loadFrom(Trait.class.getResource("/grammars/" + name().toLowerCase() + ".json"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            riGrammar = new RiGrammar().loadFrom(Trait.class.getResource("/grammars/default.json"));
        }
    }
}

