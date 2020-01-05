package chessagents.ontology.schemas.actions;

import chessagents.ontology.schemas.concepts.PieceMove;
import jade.content.AgentAction;

public class MakeMove implements AgentAction {

    private PieceMove move;

    public MakeMove() {
    }

    public MakeMove(PieceMove move) {
        this.move = move;
    }

    public PieceMove getMove() {
        return move;
    }

    public void setMove(PieceMove move) {
        this.move = move;
    }

}
