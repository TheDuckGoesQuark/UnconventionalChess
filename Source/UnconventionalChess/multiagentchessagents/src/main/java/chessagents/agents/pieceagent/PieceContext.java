package chessagents.agents.pieceagent;

import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Piece;
import chessagents.ontology.schemas.concepts.Position;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PieceContext {

    private final int gameId;
    private final Colour myColour;
    private final Position myPosition;
    private final AID gameAgentAID;
    private final int maxDebateCycle;
    private final Map<AID, Piece> aidToPiece = new HashMap<>();
    private final BoardWrapper board = new BoardWrapper();

    private String moveSubscriptionId = null;

    public PieceContext(int gameId, Colour myColour, AID gameAgentAID, Position myPosition, int maxDebateCycle) {
        this.gameId = gameId;
        this.myColour = myColour;
        this.myPosition = myPosition;
        this.gameAgentAID = gameAgentAID;
        this.maxDebateCycle = maxDebateCycle;
    }

    public boolean isMyTurnToGo() {
        return board.isSideToGo(myColour.getColour());
    }
}
