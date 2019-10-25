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
        var aidToPiece = new HashMap<AID, Piece>();
        var game = new Game(Integer.parseInt((String) args[3]));
        var gameAgentAID = new AID((String) args[2], true);

        var sequence = new SequentialBehaviour();
        sequence.addSubBehaviour(new SubscribeToGameStatus(this, gameAgentAID, game));
        sequence.addSubBehaviour(new RequestPieceIds(this, aidToPiece, myColour, gameAgentAID));
        sequence.addSubBehaviour(new Play(this, myColour, aidToPiece));
        addBehaviour(sequence);
    }
}
