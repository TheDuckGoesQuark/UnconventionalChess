package chessagents.agents.pieceagent.actions;

import chessagents.chess.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.lang.acl.ACLMessage;

public class RequestToRemainSpeakerAction extends PieceAction {

    private final ACLMessage cfp;

    /**
     * @param actor piece performing the action
     * @param cfp   call for proposal messgage
     */
    public RequestToRemainSpeakerAction(ChessPiece actor, ACLMessage cfp) {
        super(PieceTransition.REQUESTED_TO_REMAIN_SPEAKER, "Request to remain speaker", actor, false);
        this.cfp = cfp;
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        var proposal = actor.constructProposalToSpeak(cfp);
        actor.send(proposal);
        return gameState;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState;
    }
}
