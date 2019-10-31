package chessagents.agents.pieceagent.behaviours.turn;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.concepts.Move;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;

import static chessagents.agents.pieceagent.behaviours.turn.Play.MOVE_KEY;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.OTHER_MOVE_RECEIVED;

public class WaitForMove extends SimpleBehaviour {

    private final PieceContext context;

    public WaitForMove(PieceAgent pieceAgent, PieceContext context, DataStore dataStore) {
        super(pieceAgent);
        setDataStore(dataStore);
        this.context = context;
    }

    @Override
    public void action() {
        var message = myAgent.receive();

        if (message != null) {
            var move = extractMove();
        } else {
            block();
        }
    }

    private Move extractMove() {
    }

    @Override
    public boolean done() {
        return getDataStore().containsKey(MOVE_KEY);
    }

    @Override
    public void reset() {
        getDataStore().remove(MOVE_KEY);
        super.reset();
    }

    @Override
    public int onEnd() {
        return OTHER_MOVE_RECEIVED.ordinal();
    }
}
