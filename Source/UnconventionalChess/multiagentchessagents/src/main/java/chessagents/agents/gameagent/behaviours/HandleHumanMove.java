package chessagents.agents.gameagent.behaviours;

import com.github.bhlangonijr.chesslib.Board;
import jade.core.behaviours.OneShotBehaviour;

public abstract class HandleHumanMove extends OneShotBehaviour {
    @Override
    public abstract void action();
        // check human is playing
        // check it is human turn to go
        // validate move
        // send move back to human if gucci

}
