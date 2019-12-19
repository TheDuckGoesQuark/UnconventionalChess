package chessagents.agents.pieceagent.personality;

import chessagents.agents.pieceagent.argumentation.actions.ConversationActionType;
import chessagents.agents.pieceagent.personality.values.EnsureMySafety;
import chessagents.agents.pieceagent.personality.values.MaximiseEnemyCapturedPieces;
import chessagents.agents.pieceagent.personality.values.MinimiseFriendlyThreatenedPieces;
import chessagents.agents.pieceagent.personality.values.Value;
import lombok.Getter;
import rita.RiGrammar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public enum Trait {
    AGGRESSIVE(new Value[]{new MaximiseEnemyCapturedPieces()}),
    DEFENSIVE(new Value[]{new MinimiseFriendlyThreatenedPieces()}),
    EGOTISTIC(new Value[]{new EnsureMySafety()}),
    BENEVOLENT(new Value[]{new MinimiseFriendlyThreatenedPieces()});

    private final Set<Value> appealingValues;
    private RiGrammar riGrammar;

    Trait(Value[] values) {
        appealingValues = new HashSet<>(Arrays.asList(values));

        try {
            riGrammar = new RiGrammar().loadFrom(getClass().getResource("grammars/" + name().toLowerCase() + ".json"));
            verifyGrammar(riGrammar);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            riGrammar = new RiGrammar().loadFrom(getClass().getResource("grammars/default.json"));
        }
    }

    private void verifyGrammar(RiGrammar riGrammar) throws Exception {
        var conversationActions = ConversationActionType.values();

        for (int i = 0; i < conversationActions.length; i++) {
            var className = conversationActions[i].getClassName();

            if (!riGrammar.hasRule(className)) {
                throw new Exception("Grammar for trait " + name() + " missing: No rule for action " + className);
            }
        }
    }
}

