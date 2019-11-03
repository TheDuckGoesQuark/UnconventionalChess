package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Move;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import java.util.Optional;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.MOVE_CONFIRMATION_RECEIVED;

public class WaitForMoveConfirmation extends SimpleBehaviour implements PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private boolean received = false;

    public WaitForMoveConfirmation(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        // TODO add timeout for no move confirmation
        var message = myAgent.receive(MessageTemplate.MatchConversationId(pieceContext.getMoveSubscriptionId()));

        if (message != null) {
            extractMove(message).ifPresent(turnContext::setCurrentMove);
        } else {
            block();
        }
    }

    private Optional<Move> extractMove(ACLMessage message) {
        Optional<Move> result = Optional.empty();
        try {
            var absEquals = (AbsPredicate) myAgent.getContentManager().extractAbsContent(message);

            if (!absEquals.getTypeName().equals(BasicOntology.EQUALS)) {
                throw new NotUnderstoodException("Did not receive expected equals predicate");
            }

            var absRight = absEquals.getAbsTerm(BasicOntology.EQUALS_RIGHT);
            var move = (Move) ChessOntology.getInstance().toObject(absRight);
            result = Optional.of(move);
            received = true;
        } catch (Codec.CodecException | OntologyException | NotUnderstoodException e) {
            logger.warning("Failed to deserialize move message: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean done() {
        return received;
    }

    @Override
    public int getNextTransition() {
        return MOVE_CONFIRMATION_RECEIVED.ordinal();
    }

    @Override
    public void reset() {
        received = false;
        super.reset();
    }
}
