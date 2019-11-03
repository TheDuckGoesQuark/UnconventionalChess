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
import java.util.Random;
import java.util.Set;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.SPEAKER_CHOSEN;

public class ChoosingSpeaker extends Behaviour implements PieceStateBehaviour {

    private final Random random = new Random();
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private final Set<ACLMessage> speakerProposals = new HashSet<>();
    private boolean done;

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
            done = true;
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
        speakerProposals.stream()
                .map(ACLMessage::createReply)
                .peek(reply -> setPerformativeForReply(reply, speaker))
                .forEach(myAgent::send);
    }

    private void setPerformativeForReply(ACLMessage reply, AID speaker) {
        var receiver = reply.getAllReceiver().next();

        if (receiver.equals(speaker)) {
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);

            // inform everyone else when done
            speakerProposals.stream()
                    .map(ACLMessage::getSender)
                    .filter(aid -> aid.equals(speaker))
                    .forEach(reply::addReplyTo);
        } else {
            reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
        }
    }

    private AID chooseSpeaker() {
        var arr = speakerProposals.toArray(new ACLMessage[0]);
        return arr[random.nextInt(arr.length)].getSender();
    }

    private boolean receivedRequestFromEveryone() {
        return speakerProposals.size() == (pieceContext.getAidToPiece().size() - 1);
    }

    @Override
    public boolean done() {
        return done;
    }

    @Override
    public int getNextTransition() {
        return SPEAKER_CHOSEN.ordinal();
    }

    @Override
    public void reset() {
        done = false;
        speakerProposals.clear();
        super.reset();
    }
}
