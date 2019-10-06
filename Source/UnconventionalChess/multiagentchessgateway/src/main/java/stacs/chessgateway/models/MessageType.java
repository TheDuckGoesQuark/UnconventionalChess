package stacs.chessgateway.models;

public enum MessageType {
    CHAT_MESSAGE("CHAT_MESSAGE"), MOVE_MESSAGE("MOVE_MESSAGE"), GAME_CONFIGURATION_MESSAGE("GAME_CONFIGURATION_MESSAGE");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }
}