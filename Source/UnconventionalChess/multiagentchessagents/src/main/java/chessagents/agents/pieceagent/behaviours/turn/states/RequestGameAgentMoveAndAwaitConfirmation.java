package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.commonbehaviours.RequestGameAgentMove;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.MakeMove;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.MessageTemplate;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.RECEIVED_CONFIRMATION_OF_MOVE_MADE;

public class RequestGameAgentMoveAndAwaitConfirmation extends SimpleBehaviour {

    enum State {
        REQUEST,
        INFORMED,
        WAITING,
        CONFIRMED,
    }

    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private RequestGameAgentMove request = null;
    private State state = State.REQUEST;

    public RequestGameAgentMoveAndAwaitConfirmation(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        switch (state) {
            case REQUEST:
                var makeMove = new MakeMove(turnContext.getCurrentMove());
                request = new RequestGameAgentMove(makeMove, pieceContext.getGameAgentAID());
                myAgent.addBehaviour(request);
                break;
            case INFORMED:
                if (request.done()) {
                    state = State.WAITING;
                    block();
                } else {
                    block();
                }
                break;
            case WAITING:
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
        state = State.REQUEST;
        super.reset();
    }

    @Override
    public int onEnd() {
        return RECEIVED_CONFIRMATION_OF_MOVE_MADE.ordinal();
    }
}
