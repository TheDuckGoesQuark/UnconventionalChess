package chessagents.agents.gatewayagent.messages;

/**
 * Types of message that can be exchanged between game agent and gateway agent.
 * <p>
 * I.e. the only messages that users of the chess agent library need to account for
 * if providing their own translation from our ontology>
 */
public enum MessageType {

    CHAT_MESSAGE("CHAT_MESSAGE"),
    MOVE_MESSAGE("MOVE_MESSAGE"),
    GAME_CONFIGURATION_MESSAGE("GAME_CONFIGURATION_MESSAGE");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }
}
