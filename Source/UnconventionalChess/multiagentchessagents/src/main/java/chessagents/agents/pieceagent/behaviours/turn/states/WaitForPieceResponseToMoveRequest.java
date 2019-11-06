package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.PIECE_AGREED_TO_MOVE;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.PIECE_REFUSED_TO_MOVE;

public class WaitForPieceResponseToMoveRequest extends SimpleBehaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private PieceTransition transition = null;
    private MessageTemplate messageTemplate;

    public WaitForPieceResponseToMoveRequest(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void onStart() {
        logCurrentState(logger, PieceState.WAIT_FOR_PIECE_RESPONSE_TO_MOVE_REQUEST);
        transition = null;

        var requestMoveMessage = turnContext.getCurrentMessage();

        messageTemplate = MessageTemplate.and(
                MessageTemplate.or(
                        MessageTemplate.MatchPerformative(ACLMessage.REFUSE),
                        MessageTemplate.MatchPerformative(ACLMessage.AGREE)
                ),
                MessageTemplate.MatchConversationId(requestMoveMessage.getConversationId())
        );
    }

    @Override
    public void action() {
        var message = myAgent.receive(messageTemplate);

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
    public int getNextTransition() {
        return transition.ordinal();
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
