package chessagents.agents.pieceagent.behaviours;

import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import jade.core.behaviours.FSMBehaviour;

public class Play extends FSMBehaviour {

    private final PieceContext context;

    public Play(PieceAgent pieceAgent, PieceContext context) {
        super(pieceAgent);
        this.context= context;
    }
}
