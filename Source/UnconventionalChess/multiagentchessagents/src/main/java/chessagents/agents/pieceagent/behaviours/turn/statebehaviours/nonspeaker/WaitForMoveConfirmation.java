package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.nonspeaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
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

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.MOVE_CONFIRMATION_RECEIVED;
import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.OTHER_PIECE_FAILED_TO_MOVE;

public class WaitForMoveConfirmation extends PieceStateBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private MessageTemplate mt = null;

    public WaitForMoveConfirmation(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.WAIT_FOR_MOVE_CONFIRMATION);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    protected void initialiseState() {
        mt = MessageTemplate.or(
                MessageTemplate.MatchConversationId(pieceContext.getMoveSubscriptionId()),
                MessageTemplate.and(
                        MessageTemplate.MatchPerformative(ACLMessage.FAILURE),
                        MessageTemplate.MatchConversationId(turnContext.getCurrentMessage().getConversationId())
                )
        );
    }

    @Override
    public void action() {
        var message = myAgent.receive(mt);

        if (message != null) {
            if (message.getPerformative() == ACLMessage.FAILURE) {
                setEvent(OTHER_PIECE_FAILED_TO_MOVE);
            } else {
                extractMove(message).ifPresent(turnContext::setCurrentMove);
                setEvent(MOVE_CONFIRMATION_RECEIVED);
            }
        } else {
            // TODO shouldn't need this check, check if we do.
            if (event == null) block();
        }
    }

    private Optional<Move> extractMove(ACLMessage message) {
        // TODO duplicated code with WaitForMove
        Optional<Move> result = Optional.empty();
        try {
            var absEquals = (AbsPredicate) myAgent.getContentManager().extractAbsContent(message);

            if (!absEquals.getTypeName().equals(BasicOntology.EQUALS)) {
                throw new NotUnderstoodException("Did not receive expected equals predicate");
            }

            var absRight = absEquals.getAbsTerm(BasicOntology.EQUALS_RIGHT);
            var move = (Move) ChessOntology.getInstance().toObject(absRight);
            result = Optional.of(move);
        } catch (Codec.CodecException | OntologyException | NotUnderstoodException e) {
            logger.warning("Failed to deserialize move message: " + e.getMessage());
        }
        return result;
    }
}
