package stacs.chessgateway.models;

import java.time.Instant;

public class MoveMessage extends TranscriptMessage {

    public static final String MESSAGE_TYPE = "MoveMessage";
    private final String pieceId;
    private final String fromCoords;
    private final String toCoords;

    public MoveMessage(Instant timestamp, String pieceId, String fromCoords, String toCoords) {
        super(timestamp);
        this.pieceId = pieceId;
        this.fromCoords = fromCoords;
        this.toCoords = toCoords;
    }

    public String getPieceId() {
        return pieceId;
    }

    public String getFromCoords() {
        return fromCoords;
    }

    public String getToCoords() {
        return toCoords;
    }

    @Override
    public String getType() {
        return MESSAGE_TYPE;
    }
}
