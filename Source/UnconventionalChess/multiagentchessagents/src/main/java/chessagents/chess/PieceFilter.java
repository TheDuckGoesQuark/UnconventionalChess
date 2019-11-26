package chessagents.chess;

import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import jade.content.OntoAID;

import java.util.function.Predicate;

public final class PieceFilter {

    public static Predicate<ChessPiece> isColour(Colour colour) {
        return p -> p.getColour().equals(colour);
    }

    public static Predicate<ChessPiece> isAgent() {
        return ChessPiece::isRepresentedByAgent;
    }

    public static Predicate<ChessPiece> isNotCaptured() {
        return ChessPiece::isOnTheBoard;
    }

    public static Predicate<ChessPiece> isAtPosition(Position position) {
        return p -> p.getPosition().equals(position);
    }

    public static Predicate<ChessPiece> hasAID(OntoAID aid) {
        return p -> p.getAgentAID().equals(aid);
    }

}
