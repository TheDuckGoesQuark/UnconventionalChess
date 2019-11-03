package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.SPEAKER_UPDATE_SENT;

public class InformEveryoneImSpeaker extends OneShotBehaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public InformEveryoneImSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public int getNextTransition() {
        return SPEAKER_UPDATE_SENT.ordinal();
    }

    @Override
    public void action() {
        logger.info("Informing everyone I'm the new speaker.");
        turnContext.setCurrentSpeaker(myAgent.getAID());

        var agreeProposal = turnContext.getCurrentMessage();
        var informSpeakerUpdated = agreeProposal.createReply();
        informSpeakerUpdated.setPerformative(ACLMessage.INFORM);
        addContent(informSpeakerUpdated);

        // TODO reply-to doesn't seem to working or is getting messed up somewhere along the line
        myAgent.send(informSpeakerUpdated);
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

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
