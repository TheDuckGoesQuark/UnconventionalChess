package chessagents.agents.pieceagent;

import chessagents.GameState;
import chessagents.agents.pieceagent.planner.PieceAction;
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

    public void performAction(PieceAgent actor, PieceAction action) {
        // have agent perform action
        action.perform(actor, gameState);

        // update game state with outcome of action
        gameState = gameState.apply(action);
    }

    public PieceAction chooseAction(Set<PieceAction> possibleActions) {
        // TODO an actual implementation
        return possibleActions.iterator().next();
    }

    public Optional<ChessPiece> getPieceForAID(AID aid) {
        return gameState.getAllPieces().stream()
                .filter(p -> p.getAgentAID().getLocalName().equals(aid.getLocalName()))
                .findFirst();
    }
}
