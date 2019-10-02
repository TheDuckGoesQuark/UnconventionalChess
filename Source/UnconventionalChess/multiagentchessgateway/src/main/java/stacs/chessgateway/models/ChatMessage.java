package stacs.chessgateway.models;

import java.time.Instant;

public class ChatMessage extends TranscriptMessage {

    public static final String MESSAGE_TYPE = "ChatMessage";

    private final String fromId;
    private final String messageBody;

    public ChatMessage(Instant timestamp, String fromId, String messageBody) {
        super(timestamp);
        this.fromId = fromId;
        this.messageBody = messageBody;
    }

    public String getFromId() {
        return fromId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    @Override
    public String getType() {
        return MESSAGE_TYPE;
    }
}
