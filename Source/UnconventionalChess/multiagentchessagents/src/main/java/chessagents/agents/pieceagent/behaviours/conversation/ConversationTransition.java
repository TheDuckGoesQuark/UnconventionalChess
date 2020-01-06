package chessagents.agents.pieceagent.behaviours.conversation;

public enum ConversationTransition {
    IS_SPEAKER,
    NOT_SPEAKER,
    SPOKE,
    MAKING_MOVE,
    MOVE_MADE,
    LISTENED,
    STARTED_SPEAKER_ELECTION,
    ASKED_TO_SPEAK,
    SPEAKER_CHOSEN,
    SPEAKER_CONFIRMED,
}
