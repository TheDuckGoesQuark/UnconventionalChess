package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.ontology.schemas.concepts.Move;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.SENT_MOVE_INFORM;
import static chessagents.agents.gameagent.behaviours.gameplay.HandleGame.MOVE_KEY;
import static chessagents.agents.gameagent.behaviours.gameplay.HandleGame.MOVE_MESSAGE_KEY;

public class SendInformMoveMessage extends OneShotBehaviour {

    private final InformSubscribersOfMoves informSubscribersOfMove;

    SendInformMoveMessage(GameAgent myAgent, DataStore datastore, InformSubscribersOfMoves informSubscribersOfMove) {
        super(myAgent);
        setDataStore(datastore);
        this.informSubscribersOfMove = informSubscribersOfMove;
    }

    @Override
    public void action() {
        var request = (ACLMessage) getDataStore().get(MOVE_MESSAGE_KEY);
        var inform = createInform(request);

        // inform requesting agent that move was successful
        myAgent.send(inform);

        // inform subscribers of move
        var move = (Move) getDataStore().get(MOVE_KEY);
        informSubscribersOfMove.addEvent(move);
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
