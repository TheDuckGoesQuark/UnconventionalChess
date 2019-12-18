package chessagents.agents.pieceagent.behaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationalAgentFSM;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.content.OntoAID;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import java.util.Optional;

public class Play extends Behaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final ConversationalAgentFSM conversationFSM;
    private MessageTemplate MOVE_MESSAGE_TEMPLATE;
    private boolean done = false;

    public Play(PieceAgent pieceAgent) {
        super(pieceAgent);
        this.conversationFSM = new ConversationalAgentFSM(pieceAgent);
    }

    @Override
    public void onStart() {
        var moveSubID = ((PieceAgent) myAgent).getPieceContext().getMoveSubscriptionId();
        MOVE_MESSAGE_TEMPLATE = MessageTemplate.and(
                MessageTemplate.MatchConversationId(moveSubID),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM)
        );
        getAgent().addBehaviour(this.conversationFSM);
    }

    @Override
    public void action() {
        var message = myAgent.receive(MOVE_MESSAGE_TEMPLATE);

        if (message != null) {
            applyMove(message);

            if (imCaptured() || gameOver()) {
                done = true;
                myAgent.removeBehaviour(this.conversationFSM);
            } else {
                // reset conversation FSM so everyone is back in initial state.
                conversationFSM.reset();
            }
        } else {
            block();
        }
    }

    private void applyMove(ACLMessage message) {
        var move = extractMove(message);

        if (move.isPresent()) {
            var myAgent = (PieceAgent) getAgent();
            myAgent.getPieceContext().makeMove(move.get());
        } else {
            logger.warning("Tried and failed to extract move from message.");
        }
    }

    private boolean gameOver() {
        var myAgent = (PieceAgent) getAgent();
        return myAgent.getPieceContext().getGameState().gameIsOver();
    }

    private boolean imCaptured() {
        var myAgent = (PieceAgent) getAgent();
        var gameState = myAgent.getPieceContext().getGameState();
        var ontoAID = new OntoAID(myAgent.getAID().getName(), AID.ISGUID);
        return gameState.getAgentPieceWithAID(ontoAID).isEmpty();
    }

    private Optional<PieceMove> extractMove(ACLMessage message) {
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

    @Override
    public boolean done() {
        return done;
    }
}
