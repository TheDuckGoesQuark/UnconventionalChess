package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.tools.sniffer.Message;
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
        var cfp = turnContext.getCurrentMessage();
        mt = MessageTemplate.MatchConversationId(cfp.getConversationId());
    }

    @Override
    public void action() {
        var response = myAgent.receive(mt);

        if (response != null) {
            switch (response.getPerformative()) {
                case ACLMessage.ACCEPT_PROPOSAL:
                    logger.info("Proposal accepted!");
                    transition = PieceTransition.CHOSEN_TO_SPEAK;
                    break;
                case ACLMessage.REJECT_PROPOSAL:
                    logger.info("Proposal rejected!");
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
    public void reset() {
        transition = null;
        mt = null;
        super.reset();
    }

    @Override
    public int getNextTransition() {
        return transition.ordinal();
    }

    @Override
    public boolean done() {
        return transition != null;
    }
}
