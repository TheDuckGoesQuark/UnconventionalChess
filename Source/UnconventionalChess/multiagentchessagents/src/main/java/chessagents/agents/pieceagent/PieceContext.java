package chessagents.agents.pieceagent;

import chessagents.GameContext;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Piece;
import chessagents.ontology.schemas.concepts.Position;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PieceContext {

    private final GameContext gameContext = new GameContext();
    private final Colour myColour;
    private final AID gameAgentAID;
    private final int maxDebateCycle;

    private String moveSubscriptionId = null;

    public PieceContext(int gameId, Colour myColour, AID gameAgentAID, Position myPosition, int maxDebateCycle) {
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
}
