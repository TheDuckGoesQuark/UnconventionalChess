package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.nonspeaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.NoAction;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.SPEAKER_UPDATED;
import static chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker.RequestSpeakerProposals.SPEAKER_CONTRACT_NET_PROTOCOL;

public class WaitForSpeakerConfirmation extends PieceStateBehaviour {

    private final TurnContext turnContext;
    private MessageTemplate mt = null;
    // TODO see if we get stuck if we get rid of this
    private boolean speakerUpdated = false;

    public WaitForSpeakerConfirmation(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.WAIT_FOR_SPEAKER_CONFIRMATION);
        this.turnContext = turnContext;
    }

    @Override
    protected void initialiseState() {
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
        var inform = myAgent.receive(mt);

        if (inform != null) {
            var newSpeaker = extractNewSpeaker(inform);
            turnContext.setCurrentSpeaker(newSpeaker);
            speakerUpdated = true;
            setChosenAction(new NoAction(SPEAKER_UPDATED, "Speaker updated", getMyPiece()));
        } else {
            if (!speakerUpdated) block();
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
}
