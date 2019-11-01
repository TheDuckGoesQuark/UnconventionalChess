package chessagents.agents.pieceagent.behaviours.turn.fsm;

public enum PieceTransition {
    MY_TURN,
    NOT_MY_TURN,
    GAME_IS_OVER,
    OTHER_MOVE_RECEIVED,
    MOVE_PERFORMED,
    TURN_ENDED,
    I_AM_LEADER,
    I_AM_NOT_LEADER,
    NOT_REQUESTING_PROPOSALS,
    ASKED_TO_MOVE,
    OTHER_PIECE_ASKED_TO_MOVE,
    ASKED_TO_PROPOSE_MOVE,
    TOLD_PIECE_TO_MAKE_MOVE,
    PIECE_AGREED_TO_MAKE_MOVE,
    AGREED_TO_MAKE_MOVE,
    ACTUALLY_MAKING_MOVE,
    RECEIVED_CONFIRMATION_OF_MOVE_MADE,
}
