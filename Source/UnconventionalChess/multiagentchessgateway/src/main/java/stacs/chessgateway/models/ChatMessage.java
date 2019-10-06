package stacs.chessgateway.models;


import com.fasterxml.jackson.core.type.TypeReference;

public class ChatMessage {

    public static final TypeReference TYPE_REFERENCE = new TypeReference<Message<ChatMessage>>() {};
    private final String fromId;
    private final String toId;
    private final String messageBody;

    public ChatMessage(String fromId, String toId, String messageBody) {
        this.fromId = fromId;
        this.toId = toId;
        this.messageBody = messageBody;
    }

    public String getFromId() {
        return fromId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getToId() {
        return toId;
    }
}
