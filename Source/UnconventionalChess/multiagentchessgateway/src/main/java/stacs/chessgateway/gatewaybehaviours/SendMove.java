package stacs.chessgateway.gatewaybehaviours;

import com.fasterxml.jackson.core.JsonProcessingException;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendMove extends OneShotBehaviour {

    private String jsonMove;

    public SendMove(String jsonMove) {
        this.jsonMove = jsonMove;
    }

    @Override
    public void action() {
        final ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        final AID pingAgent = new AID("PingAgent@chess-platform", true);
        message.addReceiver(pingAgent);
        message.setConversationId("sending-move");
        message.setContent(jsonMove);
        myAgent.send(message);
    }
}
