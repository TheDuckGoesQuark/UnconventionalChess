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
        registerTransition(INITIAL, WAIT_FOR_INITIAL_SPEAKER, MY_TURN);
        registerTransition(INITIAL, WAIT_FOR_MOVE, NOT_MY_TURN);
        registerTransition(INITIAL, GAME_OVER, GAME_IS_OVER);

        registerLastState(new GameOver(), GAME_OVER);

        // common states
        registerState(new WaitForMove(pieceAgent, pieceContext, turnContext), WAIT_FOR_MOVE);
        registerTransition(WAIT_FOR_MOVE, PERFORM_MOVE, OTHER_MOVE_RECEIVED);

        registerState(new PerformMove(pieceAgent, pieceContext, turnContext), PERFORM_MOVE);
        registerTransition(PERFORM_MOVE, END_TURN, MOVE_PERFORMED);

        registerState(new EndTurn(pieceAgent, pieceContext, turnContext), END_TURN);
        registerTransition(END_TURN, INITIAL, TURN_ENDED, PieceState.values()); // resets all states at end of turn

        registerState(new WaitForInitialSpeaker(pieceAgent, pieceContext, turnContext), WAIT_FOR_INITIAL_SPEAKER);
        registerTransition(WAIT_FOR_INITIAL_SPEAKER, DECIDE_IF_REQUESTING_PROPOSALS, I_AM_SPEAKER);
        registerTransition(WAIT_FOR_INITIAL_SPEAKER, WAIT_FOR_PROPOSAL_REQUEST, I_AM_NOT_SPEAKER);

        // speaker states
        registerState(new DecideIfRequestingProposals(pieceAgent, pieceContext, turnContext), DECIDE_IF_REQUESTING_PROPOSALS);
        registerTransition(DECIDE_IF_REQUESTING_PROPOSALS, TELL_PIECE_TO_MOVE, NOT_REQUESTING_PROPOSALS);
        registerTransition(DECIDE_IF_REQUESTING_PROPOSALS, REQUEST_SPEAKER_PROPOSALS, REQUESTING_PROPOSALS);

        registerState(new RequestSpeakerProposals(pieceAgent, pieceContext, turnContext), REQUEST_SPEAKER_PROPOSALS);
        registerTransition(REQUEST_SPEAKER_PROPOSALS, REQUEST_TO_REMAIN_SPEAKER, PROPOSALS_REQUESTED);

        registerState(new RequestToRemainSpeaker(pieceAgent, pieceContext, turnContext), REQUEST_TO_REMAIN_SPEAKER);
        registerTransition(REQUEST_TO_REMAIN_SPEAKER, CHOOSING_SPEAKER, REQUESTED_TO_REMAIN_SPEAKER);

        registerState(new ChoosingSpeaker(pieceAgent, pieceContext, turnContext), CHOOSING_SPEAKER);
        registerTransition(CHOOSING_SPEAKER, WAIT_FOR_PERMISSION_TO_SPEAK, SPEAKER_CHOSEN);

        registerState(new InformEveryoneImSpeaker(pieceAgent, pieceContext, turnContext), INFORM_EVERYONE_IM_SPEAKER);
        registerTransition(INFORM_EVERYONE_IM_SPEAKER, REACT_TO_PREVIOUS_PROPOSAL, SPEAKER_UPDATE_SENT);

        registerState(new ReactToPreviousProposal(pieceAgent, pieceContext, turnContext), REACT_TO_PREVIOUS_PROPOSAL);
        registerTransition(REACT_TO_PREVIOUS_PROPOSAL, DECIDE_IF_REQUESTING_PROPOSALS, REACTED_TO_PREVIOUS_PROPOSAL);

        registerState(new TellPieceToMove(pieceAgent, pieceContext, turnContext), TELL_PIECE_TO_MOVE);
        registerTransition(TELL_PIECE_TO_MOVE, WAIT_FOR_PROPOSAL_REQUEST, TOLD_PIECE_TO_MOVE);

        registerState(new DecideIfMoving(pieceAgent, pieceContext, turnContext), DECIDE_IF_MOVING);
        registerTransition(DECIDE_IF_MOVING, DECIDE_IF_ACTUALLY_MOVING, AGREED_TO_MAKE_MOVE);
        registerTransition(DECIDE_IF_MOVING, DECIDE_IF_REQUESTING_PROPOSALS, NOT_MOVING);

        registerState(new DecideIfActuallyMoving(pieceAgent, pieceContext, turnContext), DECIDE_IF_ACTUALLY_MOVING);
        registerTransition(DECIDE_IF_ACTUALLY_MOVING, REQUEST_MOVE_MADE, ACTUALLY_MOVING);
        registerTransition(DECIDE_IF_ACTUALLY_MOVING, REQUEST_SPEAKER_PROPOSALS, NOT_ACTUALLY_MOVING);

        registerState(new RequestMoveMade(pieceAgent, pieceContext, turnContext), REQUEST_MOVE_MADE);
        registerTransition(REQUEST_MOVE_MADE, PERFORM_MOVE, MOVE_CONFIRMATION_RECEIVED);

        // non-speaker states
        registerState(new WaitForProposalRequest(pieceAgent, pieceContext, turnContext), WAIT_FOR_PROPOSAL_REQUEST);
        registerTransition(WAIT_FOR_PROPOSAL_REQUEST, REQUEST_TO_SPEAK, PROPOSAL_REQUESTED);
        registerTransition(WAIT_FOR_PROPOSAL_REQUEST, DECIDE_IF_MOVING, TOLD_TO_MOVE);
        registerTransition(WAIT_FOR_PROPOSAL_REQUEST, WAIT_FOR_PIECE_RESPONSE_TO_MOVE_REQUEST, OTHER_PIECE_TOLD_TO_MOVE);

        registerState(new RequestToSpeak(pieceAgent, pieceContext, turnContext), REQUEST_TO_SPEAK);
        registerTransition(REQUEST_TO_SPEAK, WAIT_FOR_PERMISSION_TO_SPEAK, REQUESTED_TO_SPEAK);

        registerState(new WaitForPermissionToSpeak(pieceAgent, pieceContext, turnContext), WAIT_FOR_PERMISSION_TO_SPEAK);
        registerTransition(WAIT_FOR_PERMISSION_TO_SPEAK, INFORM_EVERYONE_IM_SPEAKER, CHOSEN_TO_SPEAK);
        registerTransition(WAIT_FOR_PERMISSION_TO_SPEAK, WAIT_FOR_SPEAKER_CONFIRMATION, REJECTED_TO_SPEAK);

        registerState(new WaitForSpeakerConfirmation(pieceAgent, pieceContext, turnContext), WAIT_FOR_SPEAKER_CONFIRMATION);
        registerTransition(WAIT_FOR_SPEAKER_CONFIRMATION, WAIT_FOR_PROPOSAL_REQUEST, SPEAKER_UPDATED);

        registerState(new WaitForPieceResponseToMoveRequest(pieceAgent, pieceContext, turnContext), WAIT_FOR_PIECE_RESPONSE_TO_MOVE_REQUEST);
        registerTransition(WAIT_FOR_PIECE_RESPONSE_TO_MOVE_REQUEST, WAIT_FOR_MOVE_CONFIRMATION, PIECE_AGREED_TO_MOVE);
        registerTransition(WAIT_FOR_PIECE_RESPONSE_TO_MOVE_REQUEST, REQUEST_TO_SPEAK, PIECE_REFUSED_TO_MOVE);

        registerState(new WaitForMoveConfirmation(pieceAgent, pieceContext, turnContext), WAIT_FOR_MOVE_CONFIRMATION);
        registerTransition(WAIT_FOR_MOVE_CONFIRMATION, PERFORM_MOVE, MOVE_CONFIRMATION_RECEIVED);
        registerTransition(WAIT_FOR_MOVE_CONFIRMATION, WAIT_FOR_PROPOSAL_REQUEST, OTHER_PIECE_FAILED_TO_MOVE);

    }
}
