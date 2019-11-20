package chessagents.agents.pieceagent.actions;

import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class AskForProposalsAction extends PieceAction {
    public AskForProposalsAction(ChessPiece actor) {
        super(PieceTransition.REQUESTING_PROPOSALS, "Ask other piece what we should do", actor);
    }
}
