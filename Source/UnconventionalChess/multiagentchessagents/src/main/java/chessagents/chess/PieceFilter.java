package chessagents.chess;

import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import jade.content.OntoAID;

import java.util.function.Predicate;

final class PieceFilter {

    static Predicate<ChessPiece> isColour(Colour colour) {
        return p -> p.getColour().equals(colour);
    }

    static Predicate<ChessPiece> isAgent() {
        return ChessPiece::isRepresentedByAgent;
    }

    static Predicate<ChessPiece> isNotCaptured() {
        return ChessPiece::isOnTheBoard;
    }

    static Predicate<ChessPiece> isAtPosition(Position position) {
        return isNotCaptured().and(p -> p.getPosition().equals(position));
    }

    static Predicate<ChessPiece> isType(String type) {
        return p -> p.getType().equals(type);
    }

    static Predicate<ChessPiece> hasAID(OntoAID aid) {
        return isAgent().and(p -> p.getAgentAID().equals(aid));
    }

}
