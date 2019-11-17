package chessagents.agents.pieceagent;

import chessagents.GameContext;
import chessagents.agents.pieceagent.planner.PieceAction;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * PieceContext provides information about the current state of the piece, and any static information such as
 * its colour or the game agent.
 */
@Getter
@Setter
public class PieceContext {

    private final GameContext gameContext = new GameContext();
    private final Colour myColour;
    private final AID gameAgentAID;
    private final int maxDebateCycle;

    private String moveSubscriptionId = null;

    public PieceContext(int gameId, Colour myColour, AID gameAgentAID, int maxDebateCycle) {
        gameContext.setGameId(gameId);
        this.myColour = myColour;
        this.gameAgentAID = gameAgentAID;
        this.maxDebateCycle = maxDebateCycle;
    }

    public void makeMove(Position from, Position to) {
        gameContext.makeMove(from, to);
    }

    public boolean isMyTurnToGo() {
        return gameContext.getBoard().isSideToGo(myColour.getColour());
    }

    public void performAction(PieceAction action) {
        // TODO
    }

    public PieceAction chooseAction(Set<PieceAction> possibleActions) {

    }
}
