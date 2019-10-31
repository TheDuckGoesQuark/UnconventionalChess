package chessagents.agents.pieceagent.behaviours.turn.fsm;

public enum PieceState {
    INITIAL,
    WAIT_FOR_MOVE,
    WAIT_FOR_LEADER,
    GAME_OVER,
    PERFORM_MOVE,
    END_TURN,

}
