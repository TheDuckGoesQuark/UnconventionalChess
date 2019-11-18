package chessagents.agents.pieceagent.planner.actions;

import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.planner.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;

public class AskForProposalsAction extends PieceAction {
    public AskForProposalsAction(ChessPiece actor) {
        super(PieceTransition.REQUESTING_PROPOSALS, "Ask other piece what we should do", actor);
    }
}
