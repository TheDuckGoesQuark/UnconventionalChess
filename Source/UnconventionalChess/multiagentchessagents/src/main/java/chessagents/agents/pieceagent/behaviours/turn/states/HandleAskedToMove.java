package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.MakeMove;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.AGREED_TO_MAKE_MOVE;

public class HandleAskedToMove extends SimpleBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private boolean agreed = false;

    public HandleAskedToMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var message = turnContext.getCurrentMessage();
        sendAgree(message);
        saveMove(message);
        agreed = true;
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

    @Override
    public boolean done() {
        return agreed;
    }

    @Override
    public int onEnd() {
        return AGREED_TO_MAKE_MOVE.ordinal();
    }
}
