package chessagents.agents.pieceagent;

import chessagents.agents.ChessAgent;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;

public class PieceAgent extends ChessAgent {

    private Position myPosition = new Position();
    private Colour myColour = new Colour();

    @Override
    protected void setup() {
        super.setup();
        Object[] args = getArguments();
        myPosition.setCoordinates((String) args[0]);
        myColour.setColour((String) args[1]);
    }
}
