package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameAgentContext;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.PERFORMED_MOVE;
import static chessagents.agents.gameagent.behaviours.gameplay.HandleGame.MOVE_KEY;

public class PerformMove extends OneShotBehaviour {

    private final GameAgentContext context;

    PerformMove(GameAgent myAgent, GameAgentContext context, DataStore datastore) {
        super(myAgent);
        setDataStore(datastore);
        this.context = context;
    }

    @Override
    public void action() {
        var move = (PieceMove) getDataStore().get(MOVE_KEY);
        context.makeMove(move);
    }

    @Override
    public int onEnd() {
        return PERFORMED_MOVE.ordinal();
    }
}
