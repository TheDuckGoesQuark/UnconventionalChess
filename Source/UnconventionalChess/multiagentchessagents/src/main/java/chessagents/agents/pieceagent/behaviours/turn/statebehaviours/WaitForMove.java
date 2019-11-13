package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Move;
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

    private final PieceContext pieceContext;
    private final TurnContext turnContext;
    private MessageTemplate messageTemplate;

    public WaitForMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.WAIT_FOR_MOVE);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
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
                setEvent(OTHER_MOVE_RECEIVED);
            }, () -> logger.warning("Unable to extract move from message: " + message.toString()));
        } else {
            block();
        }

    }

    private Optional<Move> extractMove(ACLMessage message) {
        // TODO move this logic to another state to be resused
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
