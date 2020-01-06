package chessagents.agents.analysis;

public interface GameLogger {
    void logMoveAndState(String from, String to, String fenBeforeMove);
}
