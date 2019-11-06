package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.*;

public class WaitForProposalRequest extends SimpleBehaviour implements PieceStateBehaviour {

    private static final MessageTemplate MESSAGE_TEMPLATE = MessageTemplate.or(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.CFP),
                    MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
            ),
            MessageTemplate.MatchPerformative(ACLMessage.INFORM)
    );
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private PieceTransition nextTransition = null;

    public WaitForProposalRequest(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    /**
     * restore this state behaviour to its initial values before proceeding
     */
    @Override
    public void onStart() {
        logCurrentState(logger, PieceState.WAIT_FOR_PROPOSAL_REQUEST);
        nextTransition = null;
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
                nextTransition = PROPOSAL_REQUESTED;
                turnContext.setDebateCycles(turnContext.getDebateCycles() + 1);
                turnContext.setCurrentMessage(message);
                break;
            case ACLMessage.REQUEST:
                if (asksMeToMove(message)) {
                    logger.info("Asked me to move!");
                    nextTransition = TOLD_TO_MOVE;
                } else {
                    logger.info("Asked other piece to move!");
                    nextTransition = OTHER_PIECE_TOLD_TO_MOVE;
                }

                turnContext.setCurrentMessage(message);
                break;
            case ACLMessage.INFORM:
                logger.info("DUMB STUPID FKN MACHINE: " + message.toString());
                logger.info("Move arrived late.");
                nextTransition = MOVE_RECEIVED_LATE;
                // TODO better verify this
                // TODO extract move
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

    @Override
    public boolean done() {
        return nextTransition != null;
    }

    @Override
    public int getNextTransition() {
        return (nextTransition != null ? nextTransition : OTHER_PIECE_TOLD_TO_MOVE).ordinal();
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
