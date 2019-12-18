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
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForSpeakerCFP extends ConversationStateBehaviour {

    private MessageTemplate mt;

    public WaitForSpeakerCFP(PieceAgent pieceAgent, ConversationContext conversationContext) {
        super(pieceAgent, ConversationState.WAIT_FOR_SPEAKER_CFP, conversationContext);
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
            myAgent.send(constructProposalToSpeak(message));
            setTransition(ConversationTransition.ASKED_TO_SPEAK);
        } else {
            block();
        }
    }

    public ACLMessage constructProposalToSpeak(ACLMessage cfp) {
        // Construct message
        var proposal = cfp.createReply();
        proposal.setPerformative(ACLMessage.PROPOSE);

        // Construct contents
        var myAID = myAgent.getAID();
        var ontoAID = new OntoAID(myAID.getName(), AID.ISGUID);
        var becomeSpeaker = new BecomeSpeaker(ontoAID);
        var action = new Action(myAID, becomeSpeaker);
        try {
            myAgent.getContentManager().fillContent(proposal, action);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to build proposal to become speaker: " + e.getMessage());
        }

        return proposal;
    }
}
