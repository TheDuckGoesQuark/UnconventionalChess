package chessagents.agents.pieceagent.behaviours.turn.fsm;

/**
 * Interface that ensures that these methods are implemented by all behaviours that are used for states
 * in an FSM behaviour because I'm very forgetful.
 */
public interface PieceStateBehaviour {

    int getNextTransition();

    /**
     * Override behaviour onEnd to always fetch the next transition
     *
     * @return int value of next transition ordinal
     */
    default int onEnd() {
        return getNextTransition();
    }

}
