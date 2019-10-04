package stacs.chessgateway.models;

import java.time.Instant;

public class MoveMessage extends TranscriptMessage {

    public static final String MESSAGE_TYPE = "MoveMessage";
    private final String piece;
    private final String sourceSquare;
    private final String targetSquare;

    public MoveMessage(Instant timestamp, String piece, String sourceSquare, String targetSquare) {
        super(timestamp);
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
    public String getType() {
        return MESSAGE_TYPE;
    }
}
