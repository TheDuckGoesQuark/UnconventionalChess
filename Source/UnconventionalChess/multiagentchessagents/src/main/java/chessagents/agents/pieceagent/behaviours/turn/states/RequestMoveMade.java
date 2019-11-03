package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.commonbehaviours.RequestGameAgentMove;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.MakeMove;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.MessageTemplate;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.MOVE_CONFIRMATION_RECEIVED;

public class RequestMoveMade extends SimpleBehaviour implements PieceStateBehaviour {

    enum State {
        MAKE_REQUEST,
        WAITING_FOR_INFORM,
        WAITING_FOR_CONFIRMATION,
        CONFIRMED,
    }

    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private RequestGameAgentMove request = null;
    private State state = State.MAKE_REQUEST;

    public RequestMoveMade(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        switch (state) {
            case MAKE_REQUEST:
                var makeMove = new MakeMove(turnContext.getCurrentMove());
                request = new RequestGameAgentMove(makeMove, pieceContext.getGameAgentAID());
                myAgent.addBehaviour(request);
                state = State.WAITING_FOR_INFORM;
                break;
            case WAITING_FOR_INFORM:
                if (request.done()) {
                    state = State.WAITING_FOR_CONFIRMATION;
                } else {
                    block();
                }
                break;
            case WAITING_FOR_CONFIRMATION:
                var message = myAgent.receive(MessageTemplate.MatchConversationId(pieceContext.getMoveSubscriptionId()));

                if (message != null) {
                    state = State.CONFIRMED;
                } else {
                    block();
                }
                break;
        }
    }

    @Override
    public boolean done() {
        return state == State.CONFIRMED;
    }

    @Override
    public void reset() {
        state = State.MAKE_REQUEST;
        super.reset();
    }

    @Override
    public int getNextTransition() {
        return MOVE_CONFIRMATION_RECEIVED.ordinal();
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
