package chessagents.agents.pieceagent.actions;

import chessagents.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import lombok.Getter;

import java.util.Set;

@Getter
public class ChoosePieceToSpeakAction extends PieceAction {

    private final ChessPiece chessPieceToSpeak;
    private final Set<ACLMessage> speakerProposals;

    public ChoosePieceToSpeakAction(ChessPiece actor, ChessPiece chessPieceToSpeak, Set<ACLMessage> speakerProposals) {
        super(PieceTransition.SPEAKER_CHOSEN, "Choose piece to speak", actor);
        this.chessPieceToSpeak = chessPieceToSpeak;
        this.speakerProposals = speakerProposals;
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        sendResults(actor, chessPieceToSpeak.getAgentAID());
        return gameState;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState;
    }

    private void sendResults(PieceAgent actor, AID speaker) {
        speakerProposals.stream()
                .map(proposal -> constructReply(proposal, speaker))
                .forEach(actor::send);
    }

    private ACLMessage constructReply(ACLMessage proposal, AID speaker) {
        var reply = proposal.createReply();
        var receiver = reply.getAllReceiver().next();

        if (receiver.equals(speaker)) {
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            addReplyToAllRejected(reply, speaker);
        } else {
            reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
        }

        return reply;
    }

    private void addReplyToAllRejected(ACLMessage reply, AID speaker) {
        // inform everyone else when they've became the speaker
        speakerProposals.stream()
                .map(ACLMessage::getSender)
                .filter(aid -> !aid.equals(speaker))
                .forEach(reply::addReplyTo);
    }
}
