package chessagents.agents.pieceagent.behaviours.turn.states.nonspeaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.behaviours.turn.states.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

public class WaitForPermissionToSpeak extends SimpleBehaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private PieceTransition transition = null;
    private MessageTemplate mt = null;

    public WaitForPermissionToSpeak(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void onStart() {
        logCurrentState(logger, PieceState.WAIT_FOR_PERMISSION_TO_SPEAK);
        transition = null;

        var cfp = turnContext.getCurrentMessage();
        mt = MessageTemplate.and(
                MessageTemplate.MatchConversationId(cfp.getConversationId()),
                MessageTemplate.or(
                        MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
                        MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL)
                )
        );
    }

    @Override
    public void action() {
        var response = myAgent.receive(mt);

        if (response != null) {
            switch (response.getPerformative()) {
                case ACLMessage.ACCEPT_PROPOSAL:
                    logger.info("Proposal accepted!");
                    turnContext.setCurrentMessage(response);
                    transition = PieceTransition.CHOSEN_TO_SPEAK;
                    break;
                case ACLMessage.REJECT_PROPOSAL:
                    logger.info("Proposal rejected!");
                    turnContext.setCurrentMessage(response);
                    transition = PieceTransition.REJECTED_TO_SPEAK;
                    break;
                default:
                    logger.warning("Unknown response to CFP: " + response.toString());
                    block();
            }
        } else {
            block();
        }
    }

    @Override
    public int getNextTransition() {
        return transition.ordinal();
    }

    @Override
    public boolean done() {
        return transition != null;
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
