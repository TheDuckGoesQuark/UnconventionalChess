package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import chessagents.util.Channel;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.*;

public class VerifyMove extends Behaviour {

    private final GameAgent myAgent;
    private final GameContext context;
    private final Channel<ACLMessage> moveMessageChannel;

    private GamePlayTransition nextTransition = null;

    VerifyMove(GameAgent myAgent, GameContext context, Channel<ACLMessage> moveMessageChannel) {
        this.myAgent = myAgent;
        this.context = context;
        this.moveMessageChannel = moveMessageChannel;
    }

    @Override
    public void action() {
        var message = moveMessageChannel.receive();


    }

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public int onEnd() {
        return (nextTransition != null ? nextTransition : MOVE_INVALID).ordinal();
    }
}
