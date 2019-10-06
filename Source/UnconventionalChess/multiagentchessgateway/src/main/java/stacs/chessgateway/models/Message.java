package stacs.chessgateway.models;

public class Message<T> {

    private MessageType type;
    private int gameId;
    private T body;

    public Message(MessageType type, int gameId, T body) {
        this.type = type;
        this.gameId = gameId;
        this.body = body;
    }

    public Message(MessageType type, int gameId) {
        this.type = type;
        this.gameId = gameId;
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
}
