package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.AGREED_TO_MOVE;
import static chessagents.agents.gameagent.behaviours.gameplay.HandleGame.MOVE_MESSAGE_KEY;

public class AgreeToMove extends OneShotBehaviour {

    AgreeToMove(GameAgent myAgent, DataStore datastore) {
        super(myAgent);
        setDataStore(datastore);
    }

    @Override
    public void action() {
        var request = (ACLMessage) getDataStore().get(MOVE_MESSAGE_KEY);
        var reply = constructReply(request);
        myAgent.send(reply);
    }

    private ACLMessage constructReply(ACLMessage request) {
        var reply = request.createReply();
        reply.setPerformative(ACLMessage.AGREE);
        reply.setContent(request.getContent());
        return reply;
    }

    @Override
    public int onEnd() {
        return AGREED_TO_MOVE.ordinal();
    }
}
