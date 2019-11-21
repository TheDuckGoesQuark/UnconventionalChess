package chessagents.agents.pieceagent.actions;

import chessagents.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.lang.acl.ACLMessage;

public class RequestToSpeakAction extends PieceAction {
    private final ACLMessage cfp;

    /**
     * @param actor piece performing the action
     * @param cfp
     */
    public RequestToSpeakAction(ChessPiece actor, ACLMessage cfp) {
        super(PieceTransition.REQUESTED_TO_SPEAK, "Request to speak", actor);
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
