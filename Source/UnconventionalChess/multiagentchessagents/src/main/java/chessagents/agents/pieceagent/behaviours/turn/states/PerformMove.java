package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.concepts.Move;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.Play.MOVE_KEY;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.MOVE_PERFORMED;

public class PerformMove extends OneShotBehaviour {
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext context;

    public PerformMove(PieceAgent pieceAgent, PieceContext context, DataStore dataStore) {
        super(pieceAgent);
        setDataStore(dataStore);
        this.context = context;
    }

    @Override
    public void action() {
        var move = (Move) getDataStore().get(MOVE_KEY);
        var from = move.getSource().getCoordinates();
        var to = move.getTarget().getCoordinates();
        context.getBoard().makeMove(from, to);
        logger.info("Performed move " + from + to);
    }

    @Override
    public int onEnd() {
        return MOVE_PERFORMED.ordinal();
    }
}
