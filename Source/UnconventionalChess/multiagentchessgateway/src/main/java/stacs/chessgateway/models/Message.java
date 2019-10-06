package stacs.chessgateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message<T> {

    private MessageType type;
    private int gameId;
    private T body;

    public Message(MessageType type, int gameId, @JsonProperty("body") T body) {
        this.type = type;
        this.gameId = gameId;
        this.body = body;
    }

    public Message(MessageType type, int gameId) {
        this.type = type;
        this.gameId = gameId;
    }

    public Message() {
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
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
                ", gameId=" + gameId +
                ", body=" + "uh" +
                '}';
    }
}
