package chessagents.agents.pieceagent.behaviours.turn;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.NoAction;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import jade.core.behaviours.Behaviour;

public class ImCaptured extends PieceStateBehaviour {
    /**
     * Constructor ensures that each piece state behaviour corresponds to a piece state enum value, and that
     * only PieceAgents execute this behaviour.
     *
     * @param pieceContext context for the given piece
     * @param pieceAgent   pieceAgent that will execute this behaviour
     */
    ImCaptured(PieceContext pieceContext, PieceAgent pieceAgent) {
        super(pieceContext, pieceAgent, PieceState.CAPTURED);
    }

    @Override
    public void action() {
        setChosenAction(new NoAction(null, "Die", getMyPiece()));
    }
}
