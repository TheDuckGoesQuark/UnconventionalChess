package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.SENT_MOVE_INFORM;
import static chessagents.agents.gameagent.behaviours.gameplay.HandleGame.MOVE_MESSAGE_KEY;

public class SendInformMoveMessage extends OneShotBehaviour {

    SendInformMoveMessage(GameAgent myAgent, DataStore datastore) {
        super(myAgent);
        setDataStore(datastore);
    }

    @Override
    public void action() {
        var request = (ACLMessage) getDataStore().get(MOVE_MESSAGE_KEY);
        var inform = createInform(request);
        myAgent.send(inform);
    }

    private ACLMessage createInform(ACLMessage request) {
        var inform = request.createReply();
        inform.setPerformative(ACLMessage.INFORM);
        inform.setContent(request.getContent());
        return inform;
    }

    @Override
    public int onEnd() {
        return SENT_MOVE_INFORM.ordinal();
    }
}
