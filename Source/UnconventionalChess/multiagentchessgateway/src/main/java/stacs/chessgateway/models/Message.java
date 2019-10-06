package stacs.chessgateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message<T> {

    private MessageType type;
    private T body;

    public Message(MessageType type, @JsonProperty("body") T body) {
        this.type = type;
        this.body = body;
    }

    public Message(MessageType type) {
        this.type = type;
    }

    public Message() {
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", body=" + body +
                '}';
    }
}
