package moveprocessor;

import jade.core.Agent;
import moveprocessor.behaviours.ProcessMove;

public class MoveProcessorAgent extends Agent  {

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new ProcessMove());
        System.out.println(getAID());
    }
}
