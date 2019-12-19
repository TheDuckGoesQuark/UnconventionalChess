package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.ontology.schemas.concepts.PieceMove;

public abstract class ConversationAction {

    public abstract ConversationMessage perform();

    String grammarTag() {
        return "<" + getClass().getSimpleName() + ">";
    }

    protected static String getMovingPiece(MoveResponse moveResponse, PieceAgent agent) {
        var move = moveResponse.getMove().get();
        var movingPiece = agent.getPieceContext().getGameState().getPieceAtPosition(move.getSource()).get().getAgentAID().getLocalName();

        if (movingPiece.equals(agent.getAID().getLocalName())) movingPiece = "I";

        return movingPiece;
    }
}
