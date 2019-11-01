package chessagents.agents.gatewayagent.behaviours;


import chessagents.ontology.schemas.concepts.Move;
import jade.core.AID;

import java.util.concurrent.Callable;

public abstract class MessageHandler implements Callable<Void> {

    private Move move;
    private AID agentID;

    public void setMove(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    public AID getAgentID() {
        return agentID;
    }

    public void setAgentID(AID agentID) {
        this.agentID = agentID;
    }

    @Override
    public abstract Void call();

}
