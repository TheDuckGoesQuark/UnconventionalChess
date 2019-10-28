package chessagents.agents.pieceagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.behaviours.Play;
import chessagents.agents.pieceagent.behaviours.RequestPieceIds;
import chessagents.agents.pieceagent.behaviours.SubscribeToGameStatus;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Game;
import chessagents.ontology.schemas.concepts.Piece;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;

import java.util.HashMap;

public class PieceAgent extends ChessAgent {

    @Override
    protected void setup() {
        super.setup();
        var args = getArguments();

        var myColour = new Colour((String) args[1]);
        var game = new Game(Integer.parseInt((String) args[3]));
        var gameAgentAID = new AID((String) args[2], true);

        var aidToPiece = new HashMap<AID, Piece>();

        var sequence = new SequentialBehaviour();
        // wait until all pieces are ready
        sequence.addSubBehaviour(new SubscribeToGameStatus(this, gameAgentAID, game));
        // ask for the AID <-> piece mapping so we know who to talk to
        sequence.addSubBehaviour(new RequestPieceIds(this, aidToPiece, myColour, gameAgentAID));
        // start making moves
        sequence.addSubBehaviour(new Play(this, myColour, aidToPiece));
        addBehaviour(sequence);
    }
}
