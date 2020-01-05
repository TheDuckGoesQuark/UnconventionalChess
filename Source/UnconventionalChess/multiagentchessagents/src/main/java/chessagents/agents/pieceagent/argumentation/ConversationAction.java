package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.chess.GameState;
import jade.util.Logger;

public abstract class ConversationAction {

    protected final Logger logger = Logger.getMyLogger(getClass().getName());

    public abstract ConversationMessage perform();

    protected String grammarTag() {
        return "<" + getClass().getSimpleName() + ">";
    }

    protected static String getMovingPiece(MoveResponse moveResponse, PieceAgent agent, GameState gameState) {
        var move = moveResponse.getMove().get();
        var myName = agent.getAID().getLocalName();

        return gameState.getPieceAtPosition(move.getSource())
                .map(p -> p.getAgentAID() != null ? p.getAgentAID().getLocalName() : p.getType())
                .map(name -> name.equals(myName) ? "I" : name)
                .get();
    }
}
