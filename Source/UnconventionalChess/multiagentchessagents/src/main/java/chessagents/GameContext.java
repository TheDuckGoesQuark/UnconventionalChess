package chessagents;

import chessagents.chess.ChessBoard;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class GameContext {

    private final ChessBoard board = new ChessBoard();
    private int gameId;

    public List<AID> getAllPieceAgentAIDs() {
        return aidToPiece.values().stream()
                .map(ChessPiece::getAgentAID)
                .collect(Collectors.toUnmodifiableList());
    }

    public void makeMove(Position from, Position to) {
        board.makeMove(from.getCoordinates(), to.getCoordinates());

    }

    public Set<ChessPiece> getPiecesForColour(Colour colour) {
        return getAidToPiece().values()
                .stream()
                .filter(p -> p.getColour().equals(colour))
                .collect(Collectors.toSet());
    }

    public void addPiece(ChessPiece chessPiece) {
        aidToPiece.put(chessPiece.getAgentAID(), chessPiece);
    }

    public Optional<ChessPiece> getPieceAtPosition(Position source) {
        return aidToPiece.values().stream()
                .filter(p -> p.getPosition().equals(source))
                .findFirst();
    }
}
