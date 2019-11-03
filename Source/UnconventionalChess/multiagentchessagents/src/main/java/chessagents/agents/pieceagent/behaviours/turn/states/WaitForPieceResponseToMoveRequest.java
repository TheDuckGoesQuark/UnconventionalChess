package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.concepts.Piece;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.PIECE_AGREED_TO_MOVE;

public class WaitForPieceResponseToMoveRequest extends SimpleBehaviour implements PieceStateBehaviour {
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private boolean received = false;

    public WaitForPieceResponseToMoveRequest(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        // TODO handle piece refused to move
        var message = myAgent.receive();

        if (message != null && message.getPerformative() == ACLMessage.AGREE) {
            received = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return received;
    }

    @Override
    public void reset() {
        received = false;
        super.reset();
    }

    @Override
    public int getNextTransition() {
        return PIECE_AGREED_TO_MOVE.ordinal();
    }
}
