package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.NoAction;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
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

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.OTHER_MOVE_RECEIVED;

public class WaitForMove extends PieceStateBehaviour {

    private final TurnContext turnContext;
    private final PieceAction receiveOtherMoveAction;
    private MessageTemplate messageTemplate;

    public WaitForMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.WAIT_FOR_MOVE);
        this.turnContext = turnContext;
        this.receiveOtherMoveAction = new NoAction(OTHER_MOVE_RECEIVED, "Received other side move", getMyPiece());
    }

    @Override
    protected void initialiseState() {
        messageTemplate = MessageTemplate.MatchConversationId(pieceContext.getMoveSubscriptionId());
    }

    @Override
    public void action() {
        var message = myAgent.receive(messageTemplate);

        if (message != null) {
            extractMove(message).ifPresentOrElse(move -> {
                turnContext.setCurrentMove(move);
                setChosenAction(receiveOtherMoveAction);
            }, () -> logger.warning("Unable to extract move from message: " + message.toString()));
        } else {
            block();
        }

    }

    private Optional<PieceMove> extractMove(ACLMessage message) {
        // TODO move this logic to another state to be resused
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
