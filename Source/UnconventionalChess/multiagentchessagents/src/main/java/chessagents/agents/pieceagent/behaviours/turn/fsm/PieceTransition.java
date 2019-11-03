package chessagents.agents.pieceagent.behaviours.turn.fsm;

public enum PieceTransition {
    MY_TURN,
    NOT_MY_TURN,
    GAME_IS_OVER,
    OTHER_MOVE_RECEIVED,
    MOVE_PERFORMED,
    TURN_ENDED,
    I_AM_SPEAKER,
    I_AM_NOT_SPEAKER,
    NOT_REQUESTING_PROPOSALS,
    REQUESTING_PROPOSALS,
    PROPOSALS_REQUESTED,
    SPEAKER_CHOSEN,
    SPEAKER_UPDATED,
    SPEAKER_UPDATE_SENT,
    PROPOSAL_REQUESTED,
    TOLD_TO_MOVE,
    OTHER_PIECE_TOLD_TO_MOVE,
    REQUESTED_TO_SPEAK,
    REJECTED_TO_SPEAK,
    CHOSEN_TO_SPEAK,
    REACTED_TO_PREVIOUS_PROPOSAL,
    TOLD_PIECE_TO_MOVE,
    PIECE_AGREED_TO_MOVE,
    PIECE_REFUSED_TO_MOVE,
    AGREED_TO_MAKE_MOVE,
    NOT_MOVING,
    ACTUALLY_MOVING,
    NOT_ACTUALLY_MOVING,
    MOVE_CONFIRMATION_RECEIVED,
    NO_MOVE_CONFIRMATION,
    MOVE_RECEIVED_LATE,
}
