package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.nonspeaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.NoAction;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.PIECE_AGREED_TO_MOVE;
import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.PIECE_REFUSED_TO_MOVE;

public class WaitForPieceResponseToMoveRequest extends PieceStateBehaviour {

    private final TurnContext turnContext;
    private MessageTemplate messageTemplate;

    public WaitForPieceResponseToMoveRequest(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.WAIT_FOR_PIECE_RESPONSE_TO_MOVE_REQUEST);
        this.turnContext = turnContext;
    }

    @Override
    protected void initialiseState() {
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
                setChosenAction(new NoAction(PIECE_AGREED_TO_MOVE, "Piece agreed to move", getMyPiece()));
            else if (message.getPerformative() == ACLMessage.REFUSE)
                setChosenAction(new NoAction(PIECE_REFUSED_TO_MOVE, "Piece refused to move", getMyPiece()));
        } else {
            block();
        }
    }
}
