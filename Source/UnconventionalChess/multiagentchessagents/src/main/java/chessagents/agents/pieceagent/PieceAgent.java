package chessagents.agents.pieceagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.behaviours.ChooseMove;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import com.github.bhlangonijr.chesslib.Board;

public class PieceAgent extends ChessAgent {

    private Position myPosition = new Position();
    private Colour myColour = new Colour();
    private Board board = new Board();

    @Override
    protected void setup() {
        super.setup();
        Object[] args = getArguments();
        myPosition.setCoordinates((String) args[0]);
        myColour.setColour((String) args[1]);

        addBehaviour(new ChooseMove(board, myColour));
    }
}
