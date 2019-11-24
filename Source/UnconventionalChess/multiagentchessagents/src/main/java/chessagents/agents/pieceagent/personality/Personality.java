package chessagents.agents.pieceagent.personality;

import chessagents.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;

import java.util.*;
import java.util.stream.Collectors;

public class Personality {

    private static final Random random = new Random();
    private final Set<Trait> traits;

    private Personality(Set<Trait> initialTraits) {
        this.traits = initialTraits;
    }

    public static Personality random() {
        var options = Arrays.asList(Trait.values());
        int numOfTraits = random.nextInt(options.size());
        Collections.shuffle(options);
        return new Personality(new HashSet<>(options.subList(0, numOfTraits)));
    }

    /**
     * Maps each given action to the number of values for this agent that it satisfies
     *
     * @param chessPiece piece being considered
     * @param actions    set of actions
     * @param gameState  game state to test against
     * @return each action mapped to the number of values this agent
     */
    public Map<PieceAction, Integer> scoreActions(ChessPiece chessPiece, Set<PieceAction> actions, GameState gameState) {
        return actions.stream()
                .collect(Collectors.toMap(p -> p, action -> getSatisifedTraitCount(chessPiece, action, gameState)));
    }

    /**
     * Counts the number of traits that the given action satisfies
     *
     * @param piece     piece with these traits
     * @param action    action being tested
     * @param gameState current state of the game
     * @return the number of traits that the given actions appeals to
     */
    private int getSatisifedTraitCount(ChessPiece piece, PieceAction action, GameState gameState) {
        return traits.stream()
                .map(Trait::getAppealingValues)
                .map(values -> values.stream()
                        .map(value -> value.actionMaintainsValue(piece, gameState, action))
                        .filter(maintainsValue -> maintainsValue)
                        .map(t -> 1)
                        .reduce(0, Integer::sum))
                .reduce(0, Integer::sum);
    }
}
