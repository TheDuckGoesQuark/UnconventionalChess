package chessagents.agents.pieceagent;

import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import jade.core.Agent;

public class PieceAgent extends Agent {

    private Position myPosition = new Position();
    private Colour myColour = new Colour();

    @Override
    protected void setup() {
        super.setup();
        Object[] args = getArguments();
        myPosition.setCoordinates((String) args[0]);
        myColour.setColour((String) args[1]);

        System.out.println(getAID().toString() + myPosition.getCoordinates() + myColour.getColour());
    }
}
