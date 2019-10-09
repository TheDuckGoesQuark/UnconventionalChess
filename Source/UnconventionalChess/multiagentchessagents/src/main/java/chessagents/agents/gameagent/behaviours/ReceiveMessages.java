package chessagents.agents.gameagent.behaviours;

import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Move;
import com.github.bhlangonijr.chesslib.Board;
import jade.content.ContentManager;
import jade.content.onto.basic.Action;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Set;

public class ReceiveMessages extends CyclicBehaviour {

    private Board board;
    private Set<Move> proposedMoves = new HashSet<>();

    public ReceiveMessages(Board board) {
        this.board = board;
    }

    @Override
    public void action() {
        final ACLMessage msg = myAgent.receive();

        if (msg == null) {
            block();
            return;
        }

        ContentManager contextManager = myAgent.getContentManager();
        MoveAction action = ((MoveAction) contextManager.extractContent(msg));

        switch(msg.getPerformative()) {
            case ACLMessage.REQUEST:
                replyWithAvailableMoves(msg, action);
                break;
            case ACLMessage.PROPOSE:
                considerProposedMove(msg, action);
                break;
        }
    }

    private void considerProposedMove(ACLMessage msg, MoveAction action) {
    }

    private void replyWithAvailableMoves(ACLMessage msg, MoveAction action) {
    }
}
