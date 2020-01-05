package chessagents.agents.pieceagent.personality;

import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.personality.values.Value;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;

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
        int numOfTraits = random.nextInt(options.size() - 1) + 1;
        Collections.shuffle(options);
        return new Personality(new HashSet<>(options.subList(0, numOfTraits)));
    }

    public Set<Trait> getTraitsThatHaveValue(Value value) {
        return traits.stream().filter(trait -> trait.getAppealingValues().contains(value)).collect(Collectors.toSet());
    }

    /**
     * Maps each given action the response given by this agent
     *
     * @param chessPiece piece being considered
     * @param actions    set of actions
     * @param gameState  game state to test against
     * @return the set of actions considered with this personalities response to them
     */
    public Set<MoveResponse> getResponseToMoves(ChessPiece chessPiece, Set<PieceMove> actions, GameState gameState) {
        Set<MoveResponse> set = new HashSet<>();
        for (PieceMove action : actions) {
            Set<MoveResponse> moveResponses = getMoveResponses(chessPiece, gameState, action);
            for (MoveResponse moveRespons : moveResponses) {
                set.add(moveRespons);
            }
        }
        return set;
    }

    private Set<MoveResponse> getMoveResponses(ChessPiece chessPiece, GameState gameState, PieceMove action) {
        Set<MoveResponse> set = new HashSet<>();
        for (Trait trait : traits) {
            Set<Value> values = trait.getAppealingValues();
            for (Value value : values) {
                MoveResponse moveResponse = value.getMoveResponse(chessPiece, gameState, action);
                set.add(moveResponse);
            }
        }
        return set;
    }

    public Collection<Trait> getTraits() {
        return traits;
    }
}
