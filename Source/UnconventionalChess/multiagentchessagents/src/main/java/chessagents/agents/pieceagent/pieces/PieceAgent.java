package chessagents.agents.pieceagent.pieces;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.History;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.chat.SendChatMessage;
import chessagents.agents.pieceagent.behaviours.initial.RequestPieceIds;
import chessagents.agents.pieceagent.behaviours.initial.SubscribeToGameStatus;
import chessagents.agents.pieceagent.behaviours.turn.PlayFSM;
import chessagents.agents.pieceagent.behaviours.turn.SubscribeToMoves;
import chessagents.agents.pieceagent.events.Event;
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
import jade.util.Logger;

import java.util.Random;
import java.util.Set;

public abstract class PieceAgent extends ChessAgent {

    private final History history = new History();
    private final Random random = new Random();
    private Logger logger;
    private PieceContext context;

    @Override
    protected void setup() {
        super.setup();
        logger = Logger.getMyLogger(getName());
        constructContextFromArgs();
        addInitialBehaviours();
    }

    private void constructContextFromArgs() {
        var args = getArguments();
        var myColour = (String) args[1];
        var gameAgentAID = (String) args[2];
        var gameId = Integer.parseInt((String) args[3]);
        var maxDebateCycle = Integer.parseInt((String) args[4]);
        context = new PieceContext(gameId, new Colour(myColour), new AID(gameAgentAID, AID.ISGUID), maxDebateCycle);
    }

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

        if (speaker.equals(getAID())) {
            addBehaviour(new SendChatMessage("I think I should speak.", getAID(), context.getGameAgentAID()));
        } else {
            addBehaviour(new SendChatMessage("I think " + speaker.getName() + " should speak.", getAID(), context.getGameAgentAID()));
        }

        return speaker;
    }

    public boolean requestingProposals() {
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

    public void experienceEvent(Event event) {
        switch (event.getTransition()) {
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
}
