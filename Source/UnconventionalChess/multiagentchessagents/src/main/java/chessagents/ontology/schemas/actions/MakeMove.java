package chessagents.ontology.schemas.actions;

import chessagents.ontology.schemas.concepts.Move;
import jade.content.AgentAction;

public class MakeMove implements AgentAction {

    private Move move;

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }
}
