package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.concepts.Piece;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.PIECE_AGREED_TO_MOVE;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.PIECE_REFUSED_TO_MOVE;

public class WaitForPieceResponseToMoveRequest extends SimpleBehaviour implements PieceStateBehaviour {

    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private PieceTransition transition = null;

    public WaitForPieceResponseToMoveRequest(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var message = myAgent.receive();

        if (message != null) {
            if (message.getPerformative() == ACLMessage.AGREE)
                transition = PIECE_AGREED_TO_MOVE;
            else if (message.getPerformative() == ACLMessage.REFUSE)
                transition = PIECE_REFUSED_TO_MOVE;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return transition != null;
    }

    @Override
    public void reset() {
        transition = null;
        super.reset();
    }

    @Override
    public int getNextTransition() {
        return transition.ordinal();
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
