package chessagents.agents.pieceagent;

import chessagents.agents.ChessAgent;
import chessagents.agents.pieceagent.functionality.initial.RequestPieceIds;
import chessagents.agents.pieceagent.functionality.initial.SubscribeToGameStatus;
import chessagents.agents.pieceagent.functionality.initial.SubscribeToMoves;
import chessagents.agents.pieceagent.functionality.Play;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;

public class PieceAgent extends ChessAgent {

    /**
     * Piece static properties, and container for game context
     */
    private PieceContext context;

    /**
     * Initialise piece agent using arguments and add initial behaviours
     */
    @Override
    protected void setup() {
        super.setup();
        constructContextFromArgs();
        addInitialBehaviours();
    }

    /**
     * Constructs the piece context from arguments given on agent creation
     */
    private void constructContextFromArgs() {
        var args = getArguments();
        var myPosition = new Position((String) args[0]);
        var myColour = new Colour((String) args[1]);
        var gameAgentAID = (String) args[2];
        var gameId = Integer.parseInt((String) args[3]);
        var maxDebateCycle = Integer.parseInt((String) args[4]);
        context = new PieceContext(myPosition, getAID(), gameId, new AID(gameAgentAID, AID.ISGUID), maxDebateCycle);
    }

    /**
     * Configures the sequence of behaviours that will allow this piece to recognise its fellow team members,
     * listen for game events, and actually play.
     */
    private void addInitialBehaviours() {
        var sequence = new SequentialBehaviour();
        // wait until all pieces are ready
        sequence.addSubBehaviour(new SubscribeToGameStatus(this, context));
        // ask for the AID <-> piece mapping so we know who to talk to
        sequence.addSubBehaviour(new RequestPieceIds(this, context));
        // subscribe to updates about moves
        sequence.addSubBehaviour(new SubscribeToMoves(context));
        // start making moves
        sequence.addSubBehaviour(new Play(this));
        addBehaviour(sequence);
    }

    public PieceContext getPieceContext() {
        return context;
    }
}
