package chessagents.agents.pieceagent;

import chessagents.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Colour;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.Set;

@Getter
@Setter
public class PieceContext {

    private final Colour myColour;
    private final AID gameAgentAID;
    private final int maxDebateCycle;
    private String moveSubscriptionId = null;
    private GameState gameState;

    public PieceContext(int gameId, Colour myColour, AID gameAgentAID, int maxDebateCycle) {
        this.gameState = new GameState(gameId);
        this.myColour = myColour;
        this.gameAgentAID = gameAgentAID;
        this.maxDebateCycle = maxDebateCycle;
    }

    /**
     * @return true if it is my side to go
     */
    public boolean isMyTurnToGo() {
        return gameState.isSideToGo(myColour);
    }

    /**
     * Performs the given action with the given piece as the actor, including all side effects such as sending messages
     * to other agents
     *
     * @param actor  piece agent thats performing the action
     * @param action action be performed
     */
    void performAction(PieceAgent actor, PieceAction action) {
        // have agent perform action and update this game state
        gameState = action.perform(actor, gameState);
    }

    public PieceAction chooseAction(Set<PieceAction> possibleActions) {
        // TODO an actual implementation that isn't just pick first
        return possibleActions.iterator().next();
    }

    public Optional<ChessPiece> getPieceForAID(AID aid) {
        return gameState.getAllAgentPieces().stream()
                .filter(p -> p.getAgentAID().getLocalName().equals(aid.getLocalName()))
                .findFirst();
    }
}
