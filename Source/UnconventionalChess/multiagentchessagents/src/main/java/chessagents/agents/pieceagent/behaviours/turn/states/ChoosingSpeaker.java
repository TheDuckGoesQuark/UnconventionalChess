package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import chessagents.ontology.schemas.concepts.Piece;
import chessagents.ontology.schemas.predicates.IsSpeaker;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChoosingSpeaker extends Behaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private final Set<ACLMessage> speakerProposals = new HashSet<>();

    public ChoosingSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        if (receivedRequestFromEveryone()) {
            var speaker = chooseSpeaker();
            informEveryoneOfSpeaker(speaker);
            turnContext.setCurrentSpeaker(speaker);
        } else {
            var message = myAgent.receive();

            if (message != null && isSpeakerProposal(message)) {
                speakerProposals.add(message);
            } else {
                block();
            }
        }
    }

    private boolean isSpeakerProposal(ACLMessage message) {
        var isSpeakerProposal = false;

        try {
            var action = (Action) myAgent.getContentManager().extractContent(message);
            var isSpeaker = (IsSpeaker) action.getAction();

            // ClassCastException would have occurred before this point otherwise
            isSpeakerProposal = true;
        } catch (Codec.CodecException | ClassCastException | OntologyException e) {
            logger.warning("Failed to extract content from message: " + e.getMessage());
        }

        return isSpeakerProposal;
    }

    private void informEveryoneOfSpeaker(AID speaker) {
        var rejections = new HashSet<ACLMessage>();
        ACLMessage accept = null;

        var

        for (ACLMessage proposal : speakerProposals) {
            if (proposal.getSender().equals(speaker)) {

            } else {
                var rejection = proposal.createReply();
                proposal.setPerformative(ACLMessage.REJECT_PROPOSAL);
            }
        }
    }

    private AID chooseSpeaker() {
    }

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public int getNextTransition() {
        return 0;
    }

    @Override
    public void reset() {
        super.reset();
    }

    private boolean receivedRequestFromEveryone() {
        return speakerProposals.size() == (pieceContext.getAidToPiece().size() - 1);
    }
}
