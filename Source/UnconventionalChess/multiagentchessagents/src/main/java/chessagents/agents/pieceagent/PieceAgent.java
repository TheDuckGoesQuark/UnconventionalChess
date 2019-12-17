package chessagents.agents.pieceagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.behaviours.play.SendChatMessage;
import chessagents.agents.pieceagent.behaviours.initial.RequestPieceIds;
import chessagents.agents.pieceagent.behaviours.initial.SubscribeToGameStatus;
import chessagents.agents.pieceagent.behaviours.play.PieceTransition;
import chessagents.agents.pieceagent.behaviours.play.PlayFSM;
import chessagents.agents.pieceagent.behaviours.initial.SubscribeToMoves;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.PieceConfiguration;
import chessagents.ontology.schemas.concepts.Position;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Collections;
import java.util.Set;

public class PieceAgent extends ChessAgent {

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
        var myPosition = new Position((String) args[0]);
        var myColour = new Colour((String) args[1]);
        var gameAgentAID = (String) args[2];
        var gameId = Integer.parseInt((String) args[3]);
        var maxDebateCycle = Integer.parseInt((String) args[4]);
        var pieceConfigs = (Set<PieceConfiguration>) args[5];
        context = new PieceContext(myPosition, getAID(), gameId, new AID(gameAgentAID, AID.ISGUID), maxDebateCycle);
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
     * From the set of actions, choose the one preferred by this agent
     *
     * @param possibleActions the set of possible actions that can currently be performed
     * @return the action to perform
     */
    public PieceAction chooseAction(Set<PieceAction> possibleActions) {
        return context.chooseAction(possibleActions);
    }

    /**
     * Applies the given action to the current game state
     *
     * @param action action to perform
     */
    public PieceTransition performAction(PieceAction action) {
        if (action.shouldBeVerbalised()) {
            // TODO schedule the verbal behaviour before actually performing the action
            // TODO right the verbal behaviour doesnt run till the calling behaviour completes
            action.verbalise(context)
                    .ifPresent(v -> addBehaviour(new SendChatMessage(v, getAID(), context.getGameAgentAID())));
        }

        context.performAction(this, action);

        return action.getTransition();
    }

    /**
     * Get the set of responses to the given action
     *
     * @param action action to get responses for
     * @return the set of responses to the given action
     */
    public Set<ActionResponse> getOpinionsOfAction(PieceAction action) {
        return context.getPersonality()
                .getResponseToActions(context.getMyPiece(), Collections.singleton(action), context.getGameState())
                .get(action);
    }

    public PieceContext getContext() {
        return context;
    }
}
