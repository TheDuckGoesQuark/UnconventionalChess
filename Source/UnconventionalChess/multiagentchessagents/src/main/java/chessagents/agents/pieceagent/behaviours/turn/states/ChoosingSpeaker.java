package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import jade.content.OntoAID;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsObject;
import jade.content.abs.AbsVariable;
import jade.content.lang.Codec;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.SPEAKER_CHOSEN;
import static chessagents.agents.pieceagent.behaviours.turn.states.RequestSpeakerProposals.SPEAKER_CONTRACT_NET_PROTOCOL;

public class ChoosingSpeaker extends Behaviour implements PieceStateBehaviour {

    private static final MessageTemplate mt = MessageTemplate.MatchProtocol(SPEAKER_CONTRACT_NET_PROTOCOL);
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
            var message = myAgent.receive(mt);

            if (message != null) {
                speakerProposals.add(message);
            } else {
                block();
            }
        }
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

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
