package chessagents.agents.pieceagent.behaviours.turn.states;

/**
 * Interface that ensures that these methods are implemented by all behaviours that are used for states
 * in an FSM behaviour because I'm very forgetful.
 */
public interface PieceStateBehaviour {

    int getNextTransition();

}
