package chessagents.agents.gameagent.behaviours.gameplay;

public enum GamePlayState {
    INIT,
    ELECT_LEADER_AGENT,
    HANDLE_MOVE,
    WAIT_FOR_MOVE,
    VERIFY_MOVE,
    REFUSE_MOVE,
    AGREE_TO_MOVE,
    PERFORM_MOVE,
    SEND_INFORM_MESSAGE,
    END_GAME;
}
