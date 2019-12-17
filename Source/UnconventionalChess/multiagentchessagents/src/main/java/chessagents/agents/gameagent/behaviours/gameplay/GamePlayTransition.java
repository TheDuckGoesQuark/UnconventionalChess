package chessagents.agents.gameagent.behaviours.gameplay;

public enum GamePlayTransition {
    START_TURN,
    GAME_COMPLETE,
    LEADER_AGENT_CHOSEN,
    NO_MOVE_RECEIVED,
    MOVE_RECEIVED,
    MOVE_VALID,
    MOVE_INVALID,
    MOVE_NOT_UNDERSTOOD,
    REFUSED_TO_MOVE,
    AGREED_TO_MOVE,
    PERFORMED_MOVE,
    SENT_MOVE_INFORM,
}
