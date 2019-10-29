package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayState.END_GAME;
import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayState.INIT;

/**
 * Overloaded FSMBehaviour that accepts enums as parameters
 */
abstract class GamePlayFSMBehaviour extends FSMBehaviour {

    GamePlayFSMBehaviour(GameAgent gameAgent, DataStore dataStore) {
        super(gameAgent);
        setDataStore(dataStore);
    }

    void registerState(Behaviour state, GamePlayState name) {
        super.registerState(state, name.name());
    }

    void registerFirstState(Behaviour state) {
        super.registerFirstState(state, INIT.name());
    }

    void registerLastState(Behaviour state) {
        super.registerLastState(state, END_GAME.name());
    }

    void registerTransition(GamePlayState s1, GamePlayState s2, GamePlayTransition event) {
        super.registerTransition(s1.name(), s2.name(), event.ordinal());
    }

    void registerTransition(GamePlayState s1, GamePlayState s2, GamePlayTransition event, GamePlayState[] toBeReset) {
        var toBeResetNames = new String[toBeReset.length];
        for (int i = 0; i < toBeReset.length; i++) {
            toBeResetNames[i] = toBeReset[i].name();
        }
        super.registerTransition(s1.name(), s2.name(), event.ordinal(), toBeResetNames);
    }
}
