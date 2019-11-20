package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.nonspeaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForPermissionToSpeak extends PieceStateBehaviour {

    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private MessageTemplate mt = null;

    public WaitForPermissionToSpeak(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.WAIT_FOR_PERMISSION_TO_SPEAK);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    protected void initialiseState() {
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
                    setEvent(PieceTransition.CHOSEN_TO_SPEAK);
                    break;
                case ACLMessage.REJECT_PROPOSAL:
                    logger.info("Proposal rejected!");
                    turnContext.setCurrentMessage(response);
                    setEvent(PieceTransition.REJECTED_TO_SPEAK);
                    break;
                default:
                    logger.warning("Unknown response to CFP: " + response.toString());
                    block();
            }
        } else {
            block();
        }
    }
}