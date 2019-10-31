package chessagents.agents.pieceagent.behaviours.turn.fsm;

public enum PieceTransition {
    MY_TURN,
    NOT_MY_TURN,
    GAME_IS_OVER,
    OTHER_MOVE_RECEIVED,
    MOVE_PERFORMED,
    TURN_ENDED,
}
