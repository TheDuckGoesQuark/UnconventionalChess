package chessagents.agents.pieceagent;

import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Piece;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PieceContext {

    private final int gameId;
    private final Colour myColour;
    private final AID gameAgentAID;
    private final Map<AID, Piece> aidToPiece = new HashMap<>();
    private final BoardWrapper boardWrapper = new BoardWrapper();

    public PieceContext(int gameId, Colour myColour, AID gameAgentAID) {
        this.gameId = gameId;
        this.myColour = myColour;
        this.gameAgentAID = gameAgentAID;
    }
}
