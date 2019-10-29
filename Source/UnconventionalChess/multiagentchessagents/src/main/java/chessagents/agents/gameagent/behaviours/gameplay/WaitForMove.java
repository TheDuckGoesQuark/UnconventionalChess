package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.ontology.ChessOntology;
import chessagents.util.Channel;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.MOVE_RECEIVED;
import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.NO_MOVE_RECEIVED;

public class WaitForMove extends SimpleBehaviour {

    private final MessageTemplate MT = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
            MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
    );
    private final Channel<ACLMessage> moveMessageChannel;
    private GamePlayTransition nextTransition = NO_MOVE_RECEIVED;

    WaitForMove(GameAgent myAgent, Channel<ACLMessage> moveMessageChannel) {
        super(myAgent);
        this.moveMessageChannel = moveMessageChannel;
    }

    @Override
    public void action() {
        var message = myAgent.receive(MT);
        if (message != null) {
            moveMessageChannel.send(message);
            nextTransition = MOVE_RECEIVED;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return moveMessageChannel.containsMessages();
    }

    @Override
    public void reset() {
        moveMessageChannel.clear();
        super.reset();
    }

    @Override
    public int onEnd() {
        return nextTransition.ordinal();
    }
}
