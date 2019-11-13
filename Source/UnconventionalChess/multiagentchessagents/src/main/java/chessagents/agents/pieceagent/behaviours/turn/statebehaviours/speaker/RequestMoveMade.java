package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.commonbehaviours.RequestGameAgentMove;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.schemas.actions.MakeMove;
import jade.lang.acl.MessageTemplate;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.MOVE_CONFIRMATION_RECEIVED;

public class RequestMoveMade extends PieceStateBehaviour {

    enum RequestState {
        MAKE_REQUEST,
        WAITING_FOR_INFORM,
        WAITING_FOR_CONFIRMATION,
        CONFIRMED,
    }

    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private RequestGameAgentMove request = null;
    private RequestState requestState = null;

    public RequestMoveMade(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.REQUEST_MOVE_MADE);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    protected void initialiseState() {
        requestState = RequestState.MAKE_REQUEST;
    }

    @Override
    public void action() {
        switch (requestState) {
            case MAKE_REQUEST:
                var makeMove = new MakeMove(turnContext.getCurrentMove());
                request = new RequestGameAgentMove(makeMove, pieceContext.getGameAgentAID());
                myAgent.addBehaviour(request);
                requestState = RequestState.WAITING_FOR_INFORM;
                break;
            case WAITING_FOR_INFORM:
                if (request.done()) {
                    requestState = RequestState.WAITING_FOR_CONFIRMATION;
                    myAgent.removeBehaviour(request);
                } else {
                    block();
                }
                break;
            case WAITING_FOR_CONFIRMATION:
                var message = myAgent.receive(MessageTemplate.MatchConversationId(pieceContext.getMoveSubscriptionId()));

                if (message != null) {
                    requestState = RequestState.CONFIRMED;
                } else {
                    block();
                }
                break;
            case CONFIRMED:
                setEvent(MOVE_CONFIRMATION_RECEIVED);
                break;
        }
    }
}
