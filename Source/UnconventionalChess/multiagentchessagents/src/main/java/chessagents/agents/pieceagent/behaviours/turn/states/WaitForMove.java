package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Move;
import chessagents.ontology.schemas.predicates.MoveMade;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import java.util.Optional;

import static chessagents.agents.pieceagent.behaviours.turn.Play.MOVE_KEY;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.OTHER_MOVE_RECEIVED;

public class WaitForMove extends SimpleBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext context;
    private MessageTemplate messageTemplate;

    public WaitForMove(PieceAgent pieceAgent, PieceContext context, DataStore dataStore) {
        super(pieceAgent);
        setDataStore(dataStore);
        this.context = context;
    }

    @Override
    public void onStart() {
        messageTemplate = MessageTemplate.MatchConversationId(context.getMoveSubscriptionId());
    }

    @Override
    public void action() {
        logger.info("Waiting for move...");
        var message = myAgent.receive(messageTemplate);

        if (message != null) {
            extractMove(message)
                    .ifPresent(move -> getDataStore().put(MOVE_KEY, move));
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
        } catch (Codec.CodecException | OntologyException | NotUnderstoodException e) {
            logger.warning("Failed to deserialize move message: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean done() {
        return getDataStore().containsKey(MOVE_KEY);
    }

    @Override
    public void reset() {
        getDataStore().remove(MOVE_KEY);
        super.reset();
    }

    @Override
    public int onEnd() {
        return OTHER_MOVE_RECEIVED.ordinal();
    }
}
