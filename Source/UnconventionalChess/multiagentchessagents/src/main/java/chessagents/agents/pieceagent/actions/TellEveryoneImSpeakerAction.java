package chessagents.agents.pieceagent.actions;

import chessagents.chess.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.play.PieceTransition;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class TellEveryoneImSpeakerAction extends PieceAction {

    private final ACLMessage acceptProposalMessage;

    /**
     * @param actor piece performing the action
     */
    public TellEveryoneImSpeakerAction(ChessPiece actor, ACLMessage acceptProposalMessage) {
        super(PieceTransition.SPEAKER_UPDATE_SENT, "Tell everyone I'm the speaker", actor, false);
        this.acceptProposalMessage = acceptProposalMessage;
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        var informSpeakerUpdated = acceptProposalMessage.createReply();
        informSpeakerUpdated.setPerformative(ACLMessage.INFORM);
        addContent(actor, actor.getAID(), informSpeakerUpdated);

        actor.send(informSpeakerUpdated);

        return gameState;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState;
    }

    private void addContent(PieceAgent actor, AID speakerAID, ACLMessage informSpeakerUpdated) {
        var ontoAID = new OntoAID(speakerAID.getName(), AID.ISGUID);
        var becomeSpeaker = new BecomeSpeaker(ontoAID);
        var action = new Action(speakerAID, becomeSpeaker);
        var done = new Done(action);

        try {
            actor.getContentManager().fillContent(informSpeakerUpdated, done);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to fill contents of inform new speaker: " + e.getMessage());
        }
    }
}
