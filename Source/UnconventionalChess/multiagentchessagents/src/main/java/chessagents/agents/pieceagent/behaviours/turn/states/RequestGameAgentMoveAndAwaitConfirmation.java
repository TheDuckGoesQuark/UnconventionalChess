package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;

public class RequestGameAgentMoveAndAwaitConfirmation extends SimpleBehaviour {
    public RequestGameAgentMoveAndAwaitConfirmation(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {

    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }
}
