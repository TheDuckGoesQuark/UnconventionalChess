package chessagents.agents.pieceagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.behaviours.Play;
import chessagents.agents.pieceagent.behaviours.RequestPieceIds;
import chessagents.agents.pieceagent.behaviours.SubscribeToGameStatus;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Game;
import chessagents.ontology.schemas.concepts.Piece;
import chessagents.ontology.schemas.concepts.Position;
import jade.core.AID;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SequentialBehaviour;

import java.util.HashMap;
import java.util.HashSet;

public class PieceAgent extends ChessAgent {

    public static final String AID_TO_PIECE_KEY = "AID_TO_PIECE";
    public static final String MY_POSITION_KEY = "MY_POSITION";
    public static final String MY_COLOUR_KEY = "MY_COLOUR";
    public static final String BOARD_KEY = "BOARD";
    public static final String GAME_AGENT_AID_KEY = "GAME_AGENT";
    public static final String GAME_KEY = "GAME";

    private DataStore dataStore = new DataStore();

    @Override
    protected void setup() {
        super.setup();
        var args = getArguments();
        dataStore.put(MY_POSITION_KEY, new Position((String) args[0]));
        dataStore.put(MY_COLOUR_KEY, new Colour((String) args[1]));
        dataStore.put(GAME_AGENT_AID_KEY, new AID((String) args[2], true));
        dataStore.put(GAME_KEY, new Game(Integer.parseInt((String) args[3])));
        dataStore.put(BOARD_KEY, new BoardWrapper());
        dataStore.put(AID_TO_PIECE_KEY, new HashMap<AID, Piece>());

        var sequence = new SequentialBehaviour();
        sequence.addSubBehaviour(new SubscribeToGameStatus(this, dataStore));
        sequence.addSubBehaviour(new RequestPieceIds(this, dataStore));
        sequence.addSubBehaviour(new Play(this, dataStore));
        addBehaviour(sequence);
    }
}
