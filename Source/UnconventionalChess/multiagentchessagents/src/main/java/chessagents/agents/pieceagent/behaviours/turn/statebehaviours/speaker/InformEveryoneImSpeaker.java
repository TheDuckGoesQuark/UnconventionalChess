package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.SPEAKER_UPDATE_SENT;

public class InformEveryoneImSpeaker extends PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public InformEveryoneImSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.INFORM_EVERYONE_IM_SPEAKER);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        logger.info("Informing everyone I'm the new speaker.");
        turnContext.setCurrentSpeaker(myAgent.getAID());

        var acceptProposalMessage = turnContext.getCurrentMessage();
        var informSpeakerUpdated = acceptProposalMessage.createReply();
        informSpeakerUpdated.setPerformative(ACLMessage.INFORM);
        addContent(informSpeakerUpdated);

        myAgent.send(informSpeakerUpdated);
        setEvent(SPEAKER_UPDATE_SENT);
    }

    private void addContent(ACLMessage informSpeakerUpdated) {
        var aid = turnContext.getCurrentSpeaker();
        var ontoAID = new OntoAID(aid.getName(), AID.ISGUID);
        var becomeSpeaker = new BecomeSpeaker(ontoAID);
        var action = new Action(aid, becomeSpeaker);
        var done = new Done(action);

        try {
            myAgent.getContentManager().fillContent(informSpeakerUpdated, done);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to fill contents of inform new speaker: " + e.getMessage());
        }
    }
}
