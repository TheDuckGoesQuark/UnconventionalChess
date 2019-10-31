package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.TURN_ENDED;

public class EndTurn extends OneShotBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext context;

    public EndTurn(PieceAgent pieceAgent, PieceContext context, DataStore dataStore) {
        super(pieceAgent);
        setDataStore(dataStore);
        this.context = context;
    }

    @Override
    public void action() {
        logger.info("Turn ended!");
    }

    @Override
    public int onEnd() {
        return TURN_ENDED.ordinal();
    }
}
