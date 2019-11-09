package chessagents.agents.pieceagent.pieces;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.PlayFSM;
import chessagents.agents.pieceagent.behaviours.initial.RequestPieceIds;
import chessagents.agents.pieceagent.behaviours.initial.SubscribeToGameStatus;
import chessagents.agents.pieceagent.behaviours.turn.SubscribeToMoves;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public abstract class PieceAgent extends ChessAgent {

    private Logger logger;
    private PieceContext context;

    private void constructContextFromArgs() {
        var args = getArguments();
        var startingSquare = (String) args[0];
        var myColour = (String) args[1];
        var gameAgentAID = (String) args[2];
        var gameId = Integer.parseInt((String) args[3]);
        var maxDebateCycle = Integer.parseInt((String) args[4]);
        context = new PieceContext(gameId, new Colour(myColour), new AID(gameAgentAID, AID.ISGUID), new Position(startingSquare), maxDebateCycle);
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

    @Override
    protected void setup() {
        super.setup();
        logger = Logger.getMyLogger(getName());
        constructContextFromArgs();
        addInitialBehaviours();
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

}
