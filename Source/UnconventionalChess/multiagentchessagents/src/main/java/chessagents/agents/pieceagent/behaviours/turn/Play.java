package chessagents.agents.pieceagent.behaviours.turn;

import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceFSMBehaviour;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.states.*;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import jade.core.behaviours.DataStore;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState.*;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.*;

public class Play extends PieceFSMBehaviour {

    public static final String MOVE_KEY = "_MOVE";

    public Play(PieceAgent pieceAgent, PieceContext pieceContext) {
        super(pieceAgent);
        this.setDataStore(new DataStore());

        var turnContext = new TurnContext();

        registerFirstState(new Initial(pieceAgent, pieceContext, turnContext), INITIAL);

        registerState(new WaitForMove(pieceAgent, pieceContext, getDataStore()), WAIT_FOR_MOVE);
        registerState(new PerformMove(pieceAgent, pieceContext, getDataStore()), PERFORM_MOVE);
        registerState(new EndTurn(pieceAgent, pieceContext, getDataStore()), END_TURN);
        registerState(new WaitForLeader(pieceAgent, pieceContext, turnContext, getDataStore()), WAIT_FOR_LEADER);

        registerLastState(new GameOver(), GAME_OVER);

        // initial transitions
        registerTransition(INITIAL, WAIT_FOR_MOVE, MY_TURN);
        registerTransition(INITIAL, WAIT_FOR_MOVE, NOT_MY_TURN);
        registerTransition(INITIAL, GAME_OVER, GAME_IS_OVER);

        // wait for move transitions
        registerTransition(WAIT_FOR_MOVE, PERFORM_MOVE, OTHER_MOVE_RECEIVED);

        // perform move transitions
        registerTransition(PERFORM_MOVE, END_TURN, MOVE_PERFORMED);

        // turn ended transitions, reset all other states
        registerTransition(END_TURN, INITIAL, TURN_ENDED, PieceState.values());
    }
}
