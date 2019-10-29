package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.chess.BoardWrapper;
import jade.core.behaviours.Behaviour;

import static chessagents.agents.gameagent.GameAgent.BOARD_KEY;
import static chessagents.ontology.schemas.concepts.Colour.BLACK;
import static chessagents.ontology.schemas.concepts.Colour.WHITE;

public class InitTurn extends Behaviour {

    private boolean humanPlays;
    private boolean humanPlaysAsWhite;
    private BoardWrapper board;

    public void action(boolean humanPlays, boolean humanPlaysAsWhite, BoardWrapper board) {
        this.humanPlays = humanPlays;
        this.humanPlaysAsWhite = humanPlaysAsWhite;
        this.board = board;
    }

    private boolean isHumanTurn() {
        var board = (BoardWrapper) getDataStore().get(BOARD_KEY);

        return humanPlays &&
                (humanPlaysAsWhite ? board.isSideToGo(WHITE) : board.isSideToGo(BLACK));
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }
}
