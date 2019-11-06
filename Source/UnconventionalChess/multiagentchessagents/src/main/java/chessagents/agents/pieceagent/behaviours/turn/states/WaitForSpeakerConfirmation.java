package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.SPEAKER_UPDATED;
import static chessagents.agents.pieceagent.behaviours.turn.states.RequestSpeakerProposals.SPEAKER_CONTRACT_NET_PROTOCOL;

public class WaitForSpeakerConfirmation extends SimpleBehaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private MessageTemplate mt = null;
    private boolean speakerUpdated = false;

    public WaitForSpeakerConfirmation(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void onStart() {
        speakerUpdated = false;

        var rejectProposalMessage = turnContext.getCurrentMessage();
        mt = MessageTemplate.and(
                MessageTemplate.and(
                        MessageTemplate.MatchConversationId(rejectProposalMessage.getConversationId()),
                        MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                ),
                MessageTemplate.MatchProtocol(SPEAKER_CONTRACT_NET_PROTOCOL)
        );
    }


    @Override
    public void action() {
        logger.info("Waiting for speaking confirmation");
        var inform = myAgent.receive(mt);

        if (inform != null) {
            var newSpeaker = extractNewSpeaker(inform);
            turnContext.setCurrentSpeaker(newSpeaker);
            speakerUpdated = true;
            logger.info("Speaker updated: " + newSpeaker);
        } else {
            block();
        }
    }

    private AID extractNewSpeaker(ACLMessage inform) {
        AID speaker = null;

        try {
            var done = (Done) myAgent.getContentManager().extractContent(inform);
            var becomeSpeaker = (BecomeSpeaker) ((Action) done.getAction()).getAction();
            speaker = becomeSpeaker.getAgent();
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to extract new speaker: " + e.getMessage());
        }
        return speaker;
    }

    @Override
    public int getNextTransition() {
        return SPEAKER_UPDATED.ordinal();
    }

    @Override
    public boolean done() {
        return speakerUpdated;
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
