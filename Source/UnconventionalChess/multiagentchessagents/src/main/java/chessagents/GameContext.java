package chessagents;

import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Piece;
import chessagents.ontology.schemas.concepts.Position;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

import static chessagents.ontology.schemas.concepts.Colour.BLACK;
import static chessagents.ontology.schemas.concepts.Colour.WHITE;

@Getter
@Setter
public class GameContext {

    private final Map<AID, Piece> aidToPiece = new HashMap<>();
    private final BoardWrapper board = new BoardWrapper();
    private int gameId;

    public List<AID> getAllPieceAgentAIDs() {
        return aidToPiece.values().stream()
                .map(Piece::getAgentAID)
                .collect(Collectors.toUnmodifiableList());
    }

    public void makeMove(Position from, Position to) {
        board.makeMove(from.getCoordinates(), to.getCoordinates());

        // update position of piece agent (if exist)
        aidToPiece.values().stream()
                .map(Piece::getPosition)
                .filter(p -> p.equals(from))
                .forEach(p -> p.setCoordinates(to.getCoordinates()));
    }

    public Set<Piece> getPiecesForColour(Colour colour) {
        return getAidToPiece().values()
                .stream()
                .filter(p -> p.getColour().equals(colour))
                .collect(Collectors.toSet());
    }

    public void addPiece(Piece piece) {
        aidToPiece.put(piece.getAgentAID(), piece);
    }

    public Optional<Piece> getPieceAtPosition(Position source) {
        return aidToPiece.values().stream()
                .filter(p -> p.getPosition().equals(source))
                .findFirst();
    }
}
