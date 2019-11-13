package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.nonspeaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.*;

public class WaitForProposalRequest extends PieceStateBehaviour {

    private static final MessageTemplate MESSAGE_TEMPLATE = MessageTemplate.or(
            MessageTemplate.MatchPerformative(ACLMessage.CFP),
            MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
    );
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public WaitForProposalRequest(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.WAIT_FOR_PROPOSAL_REQUEST);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var message = myAgent.receive(MESSAGE_TEMPLATE);

        if (message != null) {
            handleMessage(message);
        } else {
            block();
        }
    }

    private void handleMessage(ACLMessage message) {
        switch (message.getPerformative()) {
            case ACLMessage.CFP:
                logger.info("Call for proposal received!");
                setEvent(PROPOSAL_REQUESTED);
                turnContext.setDebateCycles(turnContext.getDebateCycles() + 1);
                turnContext.setCurrentMessage(message);
                break;
            case ACLMessage.REQUEST:
                if (asksMeToMove(message)) {
                    logger.info("Asked me to move!");
                    setEvent(TOLD_TO_MOVE);
                } else {
                    logger.info("Asked other piece to move!");
                    setEvent(OTHER_PIECE_TOLD_TO_MOVE);
                }

                turnContext.setCurrentMessage(message);
                break;
            default:
                logger.warning("RECEIVED OUT OF SEQUENCE MESSAGE: " + message.toString());
        }
    }

    private boolean asksMeToMove(ACLMessage message) {
        var asksMeToMove = false;
        try {
            var action = (Action) myAgent.getContentManager().extractContent(message);
            asksMeToMove = action.getActor().equals(myAgent.getAID());
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to read message: " + e.getMessage());
        }
        return asksMeToMove;
    }
}
