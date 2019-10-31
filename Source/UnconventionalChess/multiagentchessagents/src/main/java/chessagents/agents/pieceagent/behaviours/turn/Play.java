package chessagents.agents.pieceagent.behaviours.turn;

import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceFSMBehaviour;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState.*;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.*;

public class Play extends PieceFSMBehaviour {

    public static final String MOVE_KEY = "_MOVE";
    private final PieceContext context;

    public Play(PieceAgent pieceAgent, PieceContext context) {
        super(pieceAgent);
        this.context = context;
        this.setDataStore(new DataStore());

        registerFirstState(new Initial(pieceAgent, context), INITIAL);

        registerState(new WaitForMove(pieceAgent, context, getDataStore()), WAIT_FOR_MOVE);

        registerLastState(new GameOver(), GAME_OVER);

        // initial transitions
        registerTransition(INITIAL, WAIT_FOR_LEADER, MY_TURN);
        registerTransition(INITIAL, WAIT_FOR_MOVE, NOT_MY_TURN);
        registerTransition(INITIAL, GAME_OVER, GAME_IS_OVER);
    }
}
