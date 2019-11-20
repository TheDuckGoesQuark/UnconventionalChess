package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.NoAction;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.PieceAgent;

public class GameOver extends PieceStateBehaviour {

    public GameOver(PieceContext pieceContext, PieceAgent pieceAgent) {
        super(pieceContext, pieceAgent, PieceState.GAME_OVER);
    }

    @Override
    public void action() {
        // TODO something?
        setChosenAction(new NoAction(null, "Game Over", getMyPiece()));
    }
}
