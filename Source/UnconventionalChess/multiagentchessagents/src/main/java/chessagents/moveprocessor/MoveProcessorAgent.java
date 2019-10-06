package chessagents.moveprocessor;

import jade.core.Agent;
import chessagents.moveprocessor.behaviours.ProcessMove;

public class MoveProcessorAgent extends Agent  {
    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new ProcessMove());
    }
}
