package chessagents.agents.pieceagent.behaviours.conversation;

public enum ConversationState {
    INITIAL,
    SPEAK,
    MAKE_MOVE,
    LISTEN,
    START_SPEAKER_ELECTION,
    WAIT_FOR_SPEAKER_CFP,
    CHOOSE_SPEAKER,
    WAIT_FOR_SPEAKER_RESULTS,
    ;
}
