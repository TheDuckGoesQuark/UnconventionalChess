package chessagents.agents.pieceagent.actions;

import chessagents.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class AskForProposalsAction extends PieceAction {
    public AskForProposalsAction(ChessPiece actor) {
        super(PieceTransition.REQUESTING_PROPOSALS, "Ask other piece what we should do", actor);
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        return null;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return null;
    }
}
