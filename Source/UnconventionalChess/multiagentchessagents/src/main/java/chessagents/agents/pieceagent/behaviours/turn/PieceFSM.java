package chessagents.agents.pieceagent.behaviours.turn;

import chessagents.agents.pieceagent.PieceAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;

/**
 * Wrapper for FSM behaviour to allow enums to be used in place of ints and strings
 */
public class PieceFSM extends FSMBehaviour {

    public PieceFSM(PieceAgent pieceAgent) {
        super(pieceAgent);
    }

    public void registerState(Behaviour state, PieceState name) {
        super.registerState(state, name.name());
    }

    public void registerFirstState(Behaviour state, PieceState name) {
        super.registerFirstState(state, name.name());
    }

    public void registerLastState(Behaviour state, PieceState name) {
        super.registerLastState(state, name.name());
    }

    public void registerTransition(PieceState s1, PieceState s2, PieceTransition event) {
        super.registerTransition(s1.name(), s2.name(), event.ordinal());
    }

    public void registerTransition(PieceState s1, PieceState s2, PieceTransition event, PieceState[] toBeReset) {
        var toBeResetNames = new String[toBeReset.length];
        for (int i = 0; i < toBeReset.length; i++) {
            toBeResetNames[i] = toBeReset[i].name();
        }
        super.registerTransition(s1.name(), s2.name(), event.ordinal(), toBeResetNames);
    }
}
