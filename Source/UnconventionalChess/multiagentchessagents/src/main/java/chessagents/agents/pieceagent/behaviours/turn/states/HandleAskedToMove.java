package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.AGREED_TO_MAKE_MOVE;

public class HandleAskedToMove extends SimpleBehaviour {

    private boolean agreed = false;

    public HandleAskedToMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
    }

    @Override
    public void action() {
        var message = myAgent.receive();

        if (message != null && message.getPerformative() == ACLMessage.REQUEST) {
            sendAgree(message);
            agreed = true;
        } else {
            block();
        }
    }

    private void sendAgree(ACLMessage message) {
        var agree = message.createReply();
        message.setPerformative(ACLMessage.AGREE);
        message.removeReceiver(myAgent.getAID());
        myAgent.send(agree);
    }

    @Override
    public boolean done() {
        return agreed;
    }

    @Override
    public int onEnd() {
        return AGREED_TO_MAKE_MOVE.ordinal();
    }
}
