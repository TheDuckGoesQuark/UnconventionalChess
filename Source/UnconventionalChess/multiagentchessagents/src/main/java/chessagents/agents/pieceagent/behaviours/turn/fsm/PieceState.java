package chessagents.agents.pieceagent.behaviours.turn.fsm;

public enum PieceState {
    INITIAL,
    WAIT_FOR_MOVE,
    WAIT_FOR_INITIAL_SPEAKER,
    GAME_OVER,
    PERFORM_MOVE,
    END_TURN,
    WAIT_FOR_PROPOSAL_REQUEST,
    CHOOSING_SPEAKER,
    WAIT_FOR_SPEAKER_CONFIRMATION,
    INFORM_EVERYONE_IM_SPEAKER,
    REACT_TO_PREVIOUS_PROPOSAL,
    DECIDE_IF_REQUESTING_PROPOSALS,
    TELL_PIECE_TO_MOVE,
    WAIT_FOR_PIECE_RESPONSE_TO_MOVE_REQUEST,
    WAIT_FOR_MOVE_CONFIRMATION,
    DECIDE_IF_MOVING,
    DECIDE_IF_ACTUALLY_MOVING,
    REQUEST_MOVE_MADE,
    REQUEST_TO_SPEAK,
    WAIT_FOR_PERMISSION_TO_SPEAK
}
