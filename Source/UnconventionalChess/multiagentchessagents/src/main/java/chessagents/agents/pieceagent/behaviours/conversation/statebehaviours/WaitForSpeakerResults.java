package chessagents.agents.pieceagent.behaviours.conversation.statebehaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationContext;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationState;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationTransition;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForSpeakerResults extends ConversationStateBehaviour {

    private MessageTemplate mt;

    public WaitForSpeakerResults(PieceAgent a, ConversationContext conversationContext) {
        super(a, ConversationState.WAIT_FOR_SPEAKER_RESULTS, conversationContext);
    }

    @Override
    public void onStart() {
        super.onStart();
        mt = MessageTemplate.and(
                getConversationContext().getConversationIdMatcher(),
                MessageTemplate.MatchProtocol(StartSpeakerElection.SPEAKER_ELECTION_PROTOCOL_NAME)
        );
    }

    @Override
    public void action() {
        var message = myAgent.receive(mt);

        if (message != null) {
            var convContext = getConversationContext();

            switch (message.getPerformative()) {
                case ACLMessage.ACCEPT_PROPOSAL:
                    convContext.setSpeaker(getAgent().getPieceContext().getMyAID());
                    informOthersImSpeaker(message);
                    setTransition(ConversationTransition.SPEAKER_CONFIRMED);
                    break;
                case ACLMessage.REJECT_PROPOSAL:
                    logger.info("Rejected for Speaker");
                    break;
                case ACLMessage.INFORM:
                    convContext.setSpeaker(extractNewSpeaker(message));
                    setTransition(ConversationTransition.SPEAKER_CONFIRMED);
                    break;
            }
        } else {
            block();
        }
    }

    private OntoAID extractNewSpeaker(ACLMessage inform) {
        OntoAID speaker = null;

        try {
            var done = (Done) myAgent.getContentManager().extractContent(inform);
            var becomeSpeaker = (BecomeSpeaker) ((Action) done.getAction()).getAction();
            speaker = becomeSpeaker.getAgent();
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to extract new speaker: " + e.getMessage());
        }
        return speaker;
    }

    private void informOthersImSpeaker(ACLMessage acceptProposal) {
        var inform = acceptProposal.createReply();
        inform.setPerformative(ACLMessage.INFORM);

        var speakerAID = myAgent.getAID();
        var ontoAID = new OntoAID(speakerAID.getName(), AID.ISGUID);
        var becomeSpeaker = new BecomeSpeaker(ontoAID);
        var action = new Action(speakerAID, becomeSpeaker);
        var done = new Done(action);

        try {
            myAgent.getContentManager().fillContent(inform, done);
            myAgent.send(inform);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to fill contents of inform new speaker: " + e.getMessage());
        }
    }
}
