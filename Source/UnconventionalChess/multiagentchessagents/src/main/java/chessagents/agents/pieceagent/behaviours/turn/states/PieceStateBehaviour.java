package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import jade.util.Logger;

/**
 * Interface that ensures that these methods are implemented by all behaviours that are used for states
 * in an FSM behaviour because I'm very forgetful.
 */
public interface PieceStateBehaviour {

    default void logCurrentState(Logger logger, PieceState pieceState) {
        logger.info("STATE: " + pieceState.name());
    }

    int getNextTransition();

}
