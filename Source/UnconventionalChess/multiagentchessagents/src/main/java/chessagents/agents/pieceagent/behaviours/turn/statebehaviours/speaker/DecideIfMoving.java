package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.schemas.actions.MakeMove;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.AGREED_TO_MAKE_MOVE;
import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.NOT_MOVING;

public class DecideIfMoving extends PieceStateBehaviour {

    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public DecideIfMoving(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.DECIDE_IF_MOVING);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var message = turnContext.getCurrentMessage();
        saveMove(message);

        if (((PieceAgent) myAgent).willAgreeToMove(turnContext.getCurrentMove())) {
            sendAgree(message);
            setEvent(AGREED_TO_MAKE_MOVE);
        } else {
            setEvent(NOT_MOVING);
        }
    }

    private void saveMove(ACLMessage message) {
        try {
            var action = (Action) myAgent.getContentManager().extractContent(message);
            var makeMove = (MakeMove) action.getAction();
            turnContext.setCurrentMove(makeMove.getMove());
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed " + e.getMessage());
        }
    }

    private void sendAgree(ACLMessage message) {
        var agree = message.createReply();
        agree.setPerformative(ACLMessage.AGREE);
        agree.removeReceiver(myAgent.getAID());
        myAgent.send(agree);
    }
}
