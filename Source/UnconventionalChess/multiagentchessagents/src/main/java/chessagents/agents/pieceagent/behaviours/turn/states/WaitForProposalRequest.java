package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.*;

public class WaitForProposalRequest extends SimpleBehaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private PieceTransition nextTransition = null;

    public WaitForProposalRequest(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var message = myAgent.receive();

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
            case ACLMessage.INFORM:
                // TODO better verify this
                logger.info("Move arrived late.");
                nextTransition = MOVE_RECEIVED_LATE;
                turnContext.setCurrentMessage(message);
                break;
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
    public void reset() {
        nextTransition = null;
        super.reset();
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
