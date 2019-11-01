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

    public Play(PieceAgent pieceAgent, PieceContext pieceContext) {
        super(pieceAgent);
        this.setDataStore(new DataStore());

        var turnContext = new TurnContext();

        registerFirstState(new Initial(pieceAgent, pieceContext, turnContext), INITIAL);

        registerState(new WaitForMove(pieceAgent, pieceContext, turnContext), WAIT_FOR_MOVE);
        registerState(new PerformMove(pieceAgent, pieceContext, turnContext), PERFORM_MOVE);
        registerState(new EndTurn(pieceAgent, pieceContext, turnContext), END_TURN);
        registerState(new WaitForLeader(pieceAgent, pieceContext, turnContext), WAIT_FOR_LEADER);
        registerState(new WaitForProposalRequest(pieceAgent, pieceContext, turnContext), WAIT_FOR_PROPOSAL_REQUEST);
        registerState(new DecideIfRequestingProposals(pieceAgent, pieceContext, turnContext), DECIDE_IF_REQUESTING_PROPOSALS);
        registerState(new TellPieceToMakeMove(pieceAgent, pieceContext, turnContext), TELL_PIECE_TO_MAKE_MOVE);
        registerState(new WaitForPieceResponseToMakeMoveRequest(pieceAgent, pieceContext, turnContext), WAIT_FOR_PIECE_RESPONSE_TO_MAKE_MOVE_REQUEST);
        registerState(new WaitForMoveConfirmation(pieceAgent, pieceContext, turnContext), WAIT_FOR_MOVE_CONFIRMATION);
        registerState(new HandleAskedToMove(pieceAgent, pieceContext, turnContext), HANDLE_ASKED_TO_MOVE);
        registerState(new DecideIfActuallyMoving(pieceAgent, pieceContext, turnContext), DECIDE_IF_ACTUALLY_MOVING);
        registerState(new RequestGameAgentMoveAndAwaitConfirmation(pieceAgent, pieceContext, turnContext), REQUEST_GAME_AGENT_MOVE_AND_AWAIT_CONFIRMATION);

        registerLastState(new GameOver(), GAME_OVER);

        // initial transitions
        registerTransition(INITIAL, WAIT_FOR_LEADER, MY_TURN);
        registerTransition(INITIAL, WAIT_FOR_MOVE, NOT_MY_TURN);
        registerTransition(INITIAL, GAME_OVER, GAME_IS_OVER);

        // wait for move transitions
        registerTransition(WAIT_FOR_MOVE, PERFORM_MOVE, OTHER_MOVE_RECEIVED);

        // perform move transitions
        registerTransition(PERFORM_MOVE, END_TURN, MOVE_PERFORMED);

        // turn ended transitions, reset all other states
        registerTransition(END_TURN, INITIAL, TURN_ENDED, PieceState.values());

        // wait for leader transition
        registerTransition(WAIT_FOR_LEADER, DECIDE_IF_REQUESTING_PROPOSALS, I_AM_LEADER);
        registerTransition(WAIT_FOR_LEADER, WAIT_FOR_PROPOSAL_REQUEST, I_AM_NOT_LEADER);

        // decide if requesting proposals transitions
        registerTransition(DECIDE_IF_REQUESTING_PROPOSALS, TELL_PIECE_TO_MAKE_MOVE, NOT_REQUESTING_PROPOSALS);

        // wait for proposal request transitions
        registerTransition(WAIT_FOR_PROPOSAL_REQUEST, HANDLE_ASKED_TO_MOVE, ASKED_TO_MOVE);
        registerTransition(WAIT_FOR_PROPOSAL_REQUEST, WAIT_FOR_PIECE_RESPONSE_TO_MAKE_MOVE_REQUEST, OTHER_PIECE_ASKED_TO_MOVE);
    }
}
