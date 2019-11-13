package chessagents.agents.pieceagent.personality;

import java.util.HashSet;
import java.util.Set;

public class Personality {

    private final Set<Trait> traits = new HashSet<>();

    public boolean addTrait(Trait trait) {
        return traits.add(trait);
    }

    public boolean removeTrait(Trait trait) {
        return traits.remove(trait);
    }
}
