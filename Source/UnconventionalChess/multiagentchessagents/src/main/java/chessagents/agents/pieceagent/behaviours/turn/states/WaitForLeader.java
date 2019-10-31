package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;

public class WaitForLeader extends SimpleBehaviour {
    public WaitForLeader(PieceAgent pieceAgent, PieceContext context, DataStore dataStore) {
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return true;
    }
}
