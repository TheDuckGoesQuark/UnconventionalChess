package chessagents.agents.pieceagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.events.ToldPieceToMoveEvent;
import chessagents.agents.pieceagent.planner.History;
import chessagents.agents.pieceagent.behaviours.chat.SendChatMessage;
import chessagents.agents.pieceagent.behaviours.initial.RequestPieceIds;
import chessagents.agents.pieceagent.behaviours.initial.SubscribeToGameStatus;
import chessagents.agents.pieceagent.behaviours.turn.PlayFSM;
import chessagents.agents.pieceagent.behaviours.turn.SubscribeToMoves;
import chessagents.agents.pieceagent.events.TransitionEvent;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Move;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;
import java.util.Set;

public class PieceAgent extends ChessAgent {

    /**
     * Random generator for ensuring that behaviour does not become predictable
     */
    private final Random random = new Random();
    /**
     * Piece static properties, and container for game context
     */
    private PieceContext context;

    /**
     * Initialise piece agent using arguments and add initial behaviours
     */
    @Override
    protected void setup() {
        super.setup();
        constructContextFromArgs();
        addInitialBehaviours();
    }

    /**
     * Constructs the piece context from arguments given on agent creation
     */
    private void constructContextFromArgs() {
        var args = getArguments();
        var myColour = (String) args[1];
        var gameAgentAID = (String) args[2];
        var gameId = Integer.parseInt((String) args[3]);
        var maxDebateCycle = Integer.parseInt((String) args[4]);
        context = new PieceContext(gameId, new Colour(myColour), new AID(gameAgentAID, AID.ISGUID), maxDebateCycle);
    }

    /**
     * Configures the sequence of behaviours that will allow this piece to recognise its fellow team members,
     * listen for game events, and actually play.
     */
    private void addInitialBehaviours() {
        var sequence = new SequentialBehaviour();
        // wait until all pieces are ready
        sequence.addSubBehaviour(new SubscribeToGameStatus(this, context));
        // ask for the AID <-> piece mapping so we know who to talk to
        sequence.addSubBehaviour(new RequestPieceIds(this, context));
        // subscribe to updates about moves
        sequence.addSubBehaviour(new SubscribeToMoves(context));
        // start making moves
        sequence.addSubBehaviour(new PlayFSM(this, context));
        addBehaviour(sequence);
    }

    public ACLMessage constructProposalToSpeak(ACLMessage cfp) {
        // Construct message
        var proposal = cfp.createReply();
        proposal.setPerformative(ACLMessage.PROPOSE);

        // Construct contents
        var myAID = getAID();
        var ontoAID = new OntoAID(myAID.getName(), AID.ISGUID);
        var becomeSpeaker = new BecomeSpeaker(ontoAID);
        var action = new Action(myAID, becomeSpeaker);
        try {
            getContentManager().fillContent(proposal, action);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to build proposal to become speaker: " + e.getMessage());
        }

        return proposal;
    }

    /**
     * Chooses speaker from the given set of speaker proposals
     *
     * @param speakerProposals set of speaker proposal messages
     * @return the chosen speaker
     */
    public AID chooseSpeaker(Set<ACLMessage> speakerProposals) {
        var arr = speakerProposals.toArray(new ACLMessage[0]);
        var speaker = arr[random.nextInt(arr.length)].getSender();

//        if (speaker.equals(getAID())) {
//            addBehaviour(new SendChatMessage("I think I should speak.", getAID(), context.getGameAgentAID()));
//        } else {
//            addBehaviour(new SendChatMessage("I think " + speaker.getName() + " should speak.", getAID(), context.getGameAgentAID()));
//        }

        return speaker;
    }

    public boolean requestingProposals() {
        // TODO or not
        return random.nextBoolean();
    }

    public boolean willAgreeToMove(Move suggestedMove) {
        // TODO or not
        return true;
    }

    public boolean isActuallyMoving() {
        // TODO or not
        return true;
    }

    public Move getNextMove() {
        // TODO fetch move from plan
        return null;
    }

    /**
     * This method works similarly to the reducers in the redux.js library, where the event triggers some change to
     * the current state of the piece agent will will affect decisions made elsewhere such as next move
     * to make. The only 'side-effect' of these reducers will be chat messages sent in reaction to certain events.
     *
     * @param transitionEvent event to be experienced.
     */
    public void experienceEvent(TransitionEvent transitionEvent) {
        switch (transitionEvent.getTransition()) {
            case MY_TURN:
                break;
            case NOT_MY_TURN:
                break;
            case GAME_IS_OVER:
                break;
            case OTHER_MOVE_RECEIVED:
                break;
            case MOVE_PERFORMED:
                break;
            case TURN_ENDED:
                break;
            case I_AM_SPEAKER:
                break;
            case I_AM_NOT_SPEAKER:
                break;
            case NOT_REQUESTING_PROPOSALS:
                break;
            case REQUESTING_PROPOSALS:
                break;
            case PROPOSALS_REQUESTED:
                break;
            case REQUESTED_TO_REMAIN_SPEAKER:
                break;
            case SPEAKER_CHOSEN:
                break;
            case SPEAKER_UPDATED:
                break;
            case SPEAKER_UPDATE_SENT:
                break;
            case PROPOSAL_REQUESTED:
                break;
            case TOLD_TO_MOVE:
                break;
            case OTHER_PIECE_TOLD_TO_MOVE:
                break;
            case REQUESTED_TO_SPEAK:
                break;
            case REJECTED_TO_SPEAK:
                break;
            case CHOSEN_TO_SPEAK:
                break;
            case REACTED_TO_PREVIOUS_PROPOSAL:
                break;
            case TOLD_PIECE_TO_MOVE:
                handleToldPieceToMoveEvent((ToldPieceToMoveEvent) transitionEvent);
                break;
            case PIECE_AGREED_TO_MOVE:
                break;
            case PIECE_REFUSED_TO_MOVE:
                break;
            case AGREED_TO_MAKE_MOVE:
                break;
            case NOT_MOVING:
                break;
            case ACTUALLY_MOVING:
                break;
            case FAILED_TO_MOVE:
                break;
            case MOVE_CONFIRMATION_RECEIVED:
                break;
            case OTHER_PIECE_FAILED_TO_MOVE:
                break;
        }
    }

    private void handleToldPieceToMoveEvent(ToldPieceToMoveEvent event) {
        var move = event.getMove();
        var pieceAID = event.getToldPiece().getAgentAID();
        addBehaviour(new SendChatMessage("Hey " + pieceAID.getLocalName() + ", you should move from " + move.getSource().getCoordinates() + " to " + move.getTarget().getCoordinates(), getAID(), context.getGameAgentAID()));
    }
}
