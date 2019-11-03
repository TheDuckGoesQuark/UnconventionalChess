package chessagents.agents.pieceagent.behaviours.turn;

import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceFSM;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.states.*;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import jade.core.behaviours.DataStore;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState.*;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.*;

/**
 * Implementation of the FSM found at
 * https://www.draw.io/#G11m79C_XHxcL85d38NqvYSzVpo2LJj5q8
 */
public class PlayFSM extends PieceFSM {

    public PlayFSM(PieceAgent pieceAgent, PieceContext pieceContext) {
        super(pieceAgent);
        this.setDataStore(new DataStore());

        // Turn context is a type safe data store for any data that is shared between states
        // JADE provides a non-type safe Datastore which requires casting but I'm not about that life
        var turnContext = new TurnContext();

        registerFirstState(new Initial(pieceAgent, pieceContext, turnContext), INITIAL);
        registerLastState(new GameOver(), GAME_OVER);

        // common states
        registerState(new WaitForMove(pieceAgent, pieceContext, turnContext), WAIT_FOR_MOVE);
        registerState(new PerformMove(pieceAgent, pieceContext, turnContext), PERFORM_MOVE);
        registerState(new EndTurn(pieceAgent, pieceContext, turnContext), END_TURN);
        registerState(new WaitForInitialSpeaker(pieceAgent, pieceContext, turnContext), WAIT_FOR_INITIAL_SPEAKER);

        // speaker states
        registerState(new DecideIfRequestingProposals(pieceAgent, pieceContext, turnContext), DECIDE_IF_REQUESTING_PROPOSALS);
        registerState(new ChoosingSpeaker(pieceAgent, pieceContext, turnContext), CHOOSING_SPEAKER);
        registerState(new InformEveryoneImSpeaker(pieceAgent, pieceContext, turnContext), INFORM_EVERYONE_IM_SPEAKER);
        registerState(new ReactToPreviousProposal(pieceAgent, pieceContext, turnContext), REACT_TO_PREVIOUS_PROPOSAL);
        registerState(new ReactToPreviousProposal(pieceAgent, pieceContext, turnContext), REACT_TO_PREVIOUS_PROPOSAL);
        registerState(new TellPieceToMove(pieceAgent, pieceContext, turnContext), TELL_PIECE_TO_MOVE);
        registerState(new DecideIfMoving(pieceAgent, pieceContext, turnContext), DECIDE_IF_MOVING);
        registerState(new DecideIfActuallyMoving(pieceAgent, pieceContext, turnContext), DECIDE_IF_ACTUALLY_MOVING);
        registerState(new RequestMoveMade(pieceAgent, pieceContext, turnContext), REQUEST_MOVE_MADE);

        // non-speaker states
        registerState(new WaitForProposalRequest(pieceAgent, pieceContext, turnContext), WAIT_FOR_PROPOSAL_REQUEST);
        registerState(new RequestToSpeak(pieceAgent, pieceContext, turnContext), REQUEST_TO_SPEAK);
        registerState(new WaitForPermissionToSpeak(pieceAgent, pieceContext, turnContext), WAIT_FOR_PERMISSION_TO_SPEAK);
        registerState(new WaitForSpeakerConfirmation(pieceAgent, pieceContext, turnContext), WAIT_FOR_SPEAKER_CONFIRMATION);
        registerState(new WaitForPieceResponseToMoveRequest(pieceAgent, pieceContext, turnContext), WAIT_FOR_PIECE_RESPONSE_TO_MOVE_REQUEST);
        registerState(new WaitForMoveConfirmation(pieceAgent, pieceContext, turnContext), WAIT_FOR_MOVE_CONFIRMATION);

        // initial transitions
        registerTransition(INITIAL, WAIT_FOR_INITIAL_SPEAKER, MY_TURN);
        registerTransition(INITIAL, WAIT_FOR_MOVE, NOT_MY_TURN);
        registerTransition(INITIAL, GAME_OVER, GAME_IS_OVER);

        // wait for move transitions
        registerTransition(WAIT_FOR_MOVE, PERFORM_MOVE, OTHER_MOVE_RECEIVED);

        // perform move transitions
        registerTransition(PERFORM_MOVE, END_TURN, MOVE_PERFORMED);

        // turn ended transitions, reset all other states
        registerTransition(END_TURN, INITIAL, TURN_ENDED, PieceState.values());

        // wait for leader transition
        registerTransition(WAIT_FOR_INITIAL_SPEAKER, DECIDE_IF_REQUESTING_PROPOSALS, I_AM_SPEAKER);
        registerTransition(WAIT_FOR_INITIAL_SPEAKER, WAIT_FOR_PROPOSAL_REQUEST, I_AM_NOT_SPEAKER);

        // decide if requesting proposals transitions
        registerTransition(DECIDE_IF_REQUESTING_PROPOSALS, TELL_PIECE_TO_MOVE, NOT_REQUESTING_PROPOSALS);
        registerTransition(DECIDE_IF_REQUESTING_PROPOSALS, CHOOSING_SPEAKER, REQUESTED_PROPOSALS);

        // wait for proposal request transitions
        registerTransition(WAIT_FOR_PROPOSAL_REQUEST, DECIDE_IF_MOVING, TOLD_TO_MOVE);
        registerTransition(WAIT_FOR_PROPOSAL_REQUEST, WAIT_FOR_PIECE_RESPONSE_TO_MOVE_REQUEST, OTHER_PIECE_TOLD_TO_MOVE);

        // choosing speaker transition
        registerTransition(CHOOSING_SPEAKER, WAIT_FOR_SPEAKER_CONFIRMATION, SPEAKER_CHOSEN);

        // tell piece to make move
        registerTransition(TELL_PIECE_TO_MOVE, WAIT_FOR_PIECE_RESPONSE_TO_MOVE_REQUEST, TOLD_PIECE_TO_MAKE_MOVE);

        registerTransition(WAIT_FOR_PIECE_RESPONSE_TO_MOVE_REQUEST, WAIT_FOR_MOVE_CONFIRMATION, PIECE_AGREED_TO_MOVE);

        registerTransition(DECIDE_IF_MOVING, DECIDE_IF_ACTUALLY_MOVING, AGREED_TO_MAKE_MOVE);

        registerTransition(DECIDE_IF_ACTUALLY_MOVING, REQUEST_MOVE_MADE, ACTUALLY_MOVING);

        registerTransition(REQUEST_MOVE_MADE, PERFORM_MOVE, MOVE_CONFIRMATION_RECEIVED);

        registerTransition(WAIT_FOR_MOVE_CONFIRMATION, PERFORM_MOVE, MOVE_CONFIRMATION_RECEIVED);
    }
}
