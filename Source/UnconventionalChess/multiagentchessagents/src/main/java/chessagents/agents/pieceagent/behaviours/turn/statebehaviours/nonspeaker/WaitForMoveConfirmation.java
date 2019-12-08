package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.nonspeaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.NoAction;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Optional;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.MOVE_CONFIRMATION_RECEIVED;
import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.OTHER_PIECE_FAILED_TO_MOVE;

public class WaitForMoveConfirmation extends PieceStateBehaviour {

    private final TurnContext turnContext;
    private final PieceAction otherPieceFailedToMoveAction;
    private final PieceAction moveWasConfirmed;
    private MessageTemplate mt = null;

    public WaitForMoveConfirmation(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.WAIT_FOR_MOVE_CONFIRMATION);
        this.turnContext = turnContext;
        this.otherPieceFailedToMoveAction = new NoAction(OTHER_PIECE_FAILED_TO_MOVE, "Recognise other piece failed to move", getMyPiece());
        this.moveWasConfirmed = new NoAction(MOVE_CONFIRMATION_RECEIVED, "Recognise move confirmed", getMyPiece());
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
                setChosenAction(otherPieceFailedToMoveAction);
            } else {
                extractMove(message).ifPresent(turnContext::setCurrentMove);
                setChosenAction(moveWasConfirmed);
            }
        }
//        else {
//            // TODO shouldn't need this check, check if we do.
//            if (pieceAction == null) block();
//        }
    }

    private Optional<PieceMove> extractMove(ACLMessage message) {
        // TODO duplicated code with WaitForMove
        Optional<PieceMove> result = Optional.empty();
        try {
            var absEquals = (AbsPredicate) myAgent.getContentManager().extractAbsContent(message);

            if (!absEquals.getTypeName().equals(BasicOntology.EQUALS)) {
                throw new NotUnderstoodException("Did not receive expected equals predicate");
            }

            var absRight = absEquals.getAbsTerm(BasicOntology.EQUALS_RIGHT);
            var move = (PieceMove) ChessOntology.getInstance().toObject(absRight);
            result = Optional.of(move);
        } catch (Codec.CodecException | OntologyException | NotUnderstoodException e) {
            logger.warning("Failed to deserialize move message: " + e.getMessage());
        }
        return result;
    }
}
