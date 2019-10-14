package stacs.chessgateway.models;

import com.fasterxml.jackson.core.type.TypeReference;

public class MoveMessage {

    public static final TypeReference TYPE_REFERENCE = new TypeReference<Message<MoveMessage>>() {
    };

    private final String piece;
    private final String sourceSquare;
    private final String targetSquare;

    public MoveMessage(String piece, String sourceSquare, String targetSquare) {
        this.piece = piece;
        this.sourceSquare = sourceSquare;
        this.targetSquare = targetSquare;
    }

    public String getPiece() {
        return piece;
    }

    public String getSourceSquare() {
        return sourceSquare;
    }

    public String getTargetSquare() {
        return targetSquare;
    }

    @Override
    public String toString() {
        return "MoveMessage{" +
                "piece='" + piece + '\'' +
                ", sourceSquare='" + sourceSquare + '\'' +
                ", targetSquare='" + targetSquare + '\'' +
                '}';
    }
}
