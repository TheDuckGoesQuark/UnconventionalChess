package chessagents.agents.pieceagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.behaviours.Play;
import chessagents.agents.pieceagent.behaviours.initial.RequestPieceIds;
import chessagents.agents.pieceagent.behaviours.initial.SubscribeToGameStatus;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;

public class PieceAgent extends ChessAgent {

    @Override
    protected void setup() {
        super.setup();
        var args = getArguments();

        var startingSquare = (String) args[0];
        var myColour = (String) args[1];
        var gameAgentAID = (String) args[2];
        var gameId = Integer.parseInt((String) args[3]);

        var context = new PieceContext(gameId, new Colour(myColour), new AID(gameAgentAID, AID.ISGUID), new Position(startingSquare));

        var sequence = new SequentialBehaviour();
        // wait until all pieces are ready
        sequence.addSubBehaviour(new SubscribeToGameStatus(this, context));
        // ask for the AID <-> piece mapping so we know who to talk to
        sequence.addSubBehaviour(new RequestPieceIds(this, context));
        // start making moves
        sequence.addSubBehaviour(new Play(this, context));
        addBehaviour(sequence);
    }
}
