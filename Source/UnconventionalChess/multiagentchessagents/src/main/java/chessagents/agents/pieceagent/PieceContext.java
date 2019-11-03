package chessagents.agents.pieceagent;

import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Piece;
import chessagents.ontology.schemas.concepts.Position;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class PieceContext {

    private final int gameId;
    private final Colour myColour;
    private final AID gameAgentAID;
    private final int maxDebateCycle;
    private final Map<AID, Piece> aidToPiece = new HashMap<>();
    private final BoardWrapper board = new BoardWrapper();

    private String moveSubscriptionId = null;

    public PieceContext(int gameId, Colour myColour, AID gameAgentAID, Position myPosition, int maxDebateCycle) {
        this.gameId = gameId;
        this.myColour = myColour;
        this.gameAgentAID = gameAgentAID;
        this.maxDebateCycle = maxDebateCycle;
    }

    public void makeMove(Position from, Position to) {
        var aidToPieceNeedsUpdating = isMyTurnToGo();
        board.makeMove(from.getCoordinates(), to.getCoordinates());

        // update position of piece agent
        if (aidToPieceNeedsUpdating) {
            aidToPiece.values().stream()
                    .map(Piece::getPosition)
                    .filter(p -> p.equals(from))
                    .forEach(p -> p.setCoordinates(to.getCoordinates()));
        }
    }

    public boolean isMyTurnToGo() {
        return board.isSideToGo(myColour.getColour());
    }

    public List<AID> getAllAIDs() {
        return aidToPiece.values().stream().map(Piece::getAgentAID).collect(Collectors.toUnmodifiableList());
    }
}
