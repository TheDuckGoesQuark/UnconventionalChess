package chessagents.agents.pieceagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.behaviours.SubscribeToGameStatus;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import jade.core.AID;

import java.util.HashSet;
import java.util.Set;

public class PieceAgent extends ChessAgent {

    private Position myPosition = new Position();
    private Colour myColour = new Colour();
    private BoardWrapper board = new BoardWrapper();
    private Set<AID> pieceAgents = new HashSet<>();

    @Override
    protected void setup() {
        super.setup();
        var args = getArguments();
        myPosition.setCoordinates((String) args[0]);
        myColour.setColour((String) args[1]);
        final var gameAgent = new AID((String) args[2], true);
        addBehaviour(new SubscribeToGameStatus());
    }
}
