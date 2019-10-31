package chessagents.agents.pieceagent.behaviours.turn;

import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceFSMBehaviour;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import jade.core.behaviours.FSMBehaviour;

public class Play extends PieceFSMBehaviour {

    private final PieceContext context;

    public Play(PieceAgent pieceAgent, PieceContext context) {
        super(pieceAgent);
        this.context = context;

        registerFirstState(new Initial(pieceAgent, context), PieceState.INITIAL);

        // initial transitions
        registerTransition(PieceState.INITIAL, PieceState.WAIT_FOR_LEADER, PieceTransition.MY_TURN);
        registerTransition(PieceState.INITIAL, PieceState.WAIT_FOR_MOVE, PieceTransition.NOT_MY_TURN);
    }
}
