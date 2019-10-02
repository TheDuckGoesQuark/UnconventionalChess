package stacs.chessgateway.models;

import java.time.Instant;

public abstract class TranscriptMessage extends BaseMessage {

    private final Instant timestamp;

    public TranscriptMessage(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
