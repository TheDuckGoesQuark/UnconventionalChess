package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.*;

public class WaitForProposalRequest extends SimpleBehaviour {

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
                nextTransition = ASKED_TO_PROPOSE_MOVE;
                break;
            case ACLMessage.REQUEST:
                logger.info("Asked to move!");
                nextTransition = ASKED_TO_MOVE;
                break;
            case ACLMessage.INFORM:
                logger.info("Other piece asked to move!");
                nextTransition = OTHER_PIECE_ASKED_TO_MOVE;
                break;
        }
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
    public int onEnd() {
        return (nextTransition != null ? nextTransition : OTHER_PIECE_ASKED_TO_MOVE).ordinal();
    }
}
