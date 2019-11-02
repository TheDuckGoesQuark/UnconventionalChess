package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import chessagents.ontology.schemas.concepts.Piece;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.Random;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.NOT_REQUESTING_PROPOSALS;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.REQUESTED_PROPOSALS;

public class DecideIfRequestingProposals extends OneShotBehaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final Random random = new Random();
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private PieceTransition pieceTransition = null;

    public DecideIfRequestingProposals(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        if (turnContext.getDebateCycles() < pieceContext.getMaxDebateCycle() && requestingProposals()) {
            requestProposals();
            pieceTransition = REQUESTED_PROPOSALS;
        } else {
            pieceTransition = NOT_REQUESTING_PROPOSALS;
        }
    }

    private void requestProposals() {
        var cfp = ((ChessAgent) myAgent).constructMessage(ACLMessage.CFP);

        pieceContext.getAllAIDs().forEach(cfp::addReceiver);

        // construct message body
        var becomeSpeaker = new BecomeSpeaker();
        try {
            myAgent.getContentManager().fillContent(cfp, becomeSpeaker);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to fill content for cfp: " + e.getMessage());
        }

        myAgent.send(cfp);
    }

    private boolean requestingProposals() {
        // TODO change from random chance to personality affected decision
        return random.nextBoolean();
    }

    @Override
    public int getNextTransition() {
        return (pieceTransition != null ? pieceTransition : NOT_REQUESTING_PROPOSALS).ordinal();
    }

    @Override
    public void reset() {
        pieceTransition = null;
        super.reset();
    }
}
