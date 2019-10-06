package stacs.chessgateway.gateway.behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendMove extends OneShotBehaviour {

    private String jsonMove;
    private String agentName;

    public SendMove(String jsonMove, String agentName) {
        this.jsonMove = jsonMove;
        this.agentName = agentName;
    }

    @Override
    public void action() {
        final ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        final AID gameAgent = new AID(agentName, false);
        message.addReceiver(gameAgent);
        message.setConversationId("sending-move");
        message.setContent(jsonMove);
        myAgent.send(message);
    }
}
