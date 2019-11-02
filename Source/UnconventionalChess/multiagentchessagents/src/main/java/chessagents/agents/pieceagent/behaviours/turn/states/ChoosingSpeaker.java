package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
            sendResults(speaker);
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

            isSpeakerProposal = action.getAction() instanceof BecomeSpeaker;

            // Set content to object to avoid having to reextract later on
            message.setContentObject(action);
        } catch (Codec.CodecException | ClassCastException | OntologyException | IOException e) {
            logger.warning("Failed to extract content from message: " + e.getMessage());
        }

        return isSpeakerProposal;
    }

    private void sendResults(AID speaker) {
        // TODO work out how to allow agents to propose other pieces as speaker
//        var rejections = speakerProposals.stream().filter(proposal -> proposal.getContentObject().)
    }

    private AID chooseSpeaker() {
        // TODO
        return null;
    }

    @Override
    public boolean done() {
        // TODO
        return false;
    }

    @Override
    public int getNextTransition() {
        // TODO
        return 0;
    }

    @Override
    public void reset() {
        // TODO
        super.reset();
    }

    private boolean receivedRequestFromEveryone() {
        return speakerProposals.size() == (pieceContext.getAidToPiece().size() - 1);
    }
}
