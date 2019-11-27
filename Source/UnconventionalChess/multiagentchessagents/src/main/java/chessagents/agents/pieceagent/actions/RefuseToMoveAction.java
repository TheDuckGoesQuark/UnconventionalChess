package chessagents.agents.pieceagent.actions;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.chess.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.lang.acl.ACLMessage;
import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.features.Tense;

import java.util.List;
import java.util.Optional;

public class RefuseToMoveAction extends PieceAction {
    private static final List<String> OBJECTS = List.of("there", "position");
    private static final List<String> VERB = List.of("move", "slide", "jump", "walk", "shift", "run");
    private static final List<String> ADVERBS = List.of("quickly", "soon", "hastily", "carefully");
    private static final List<String> COMPLEMENTISERS = List.of("to", "that");
    private static final List<String> MODALS = List.of("can", "will");

    private final ACLMessage requestToMove;
    private final PieceMove requestedMove;

    /**
     * @param actor         piece performing the action
     * @param requestToMove the message that was sent to this agent requesting them to move
     * @param requestedMove the move that was requested of this agent to make
     */
    public RefuseToMoveAction(ChessPiece actor, ACLMessage requestToMove, PieceMove requestedMove) {
        super(PieceTransition.NOT_MOVING, "Refuse to move", actor, true);
        this.requestToMove = requestToMove;
        this.requestedMove = requestedMove;
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        sendRefuse(actor, requestToMove);
        return gameState;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState;
    }

    private void sendRefuse(PieceAgent actor, ACLMessage message) {
        var refuse = message.createReply();
        refuse.setPerformative(ACLMessage.REFUSE);
        refuse.removeReceiver(actor.getAID());
        actor.send(refuse);
    }

    @Override
    public Optional<String> verbalise(PieceContext context) {
        var clause = NLG_FACTORY.createClause();

        clause.setSubject("I");
        clause.setVerb(chooseRandom(VERB));
        clause.setObject(chooseRandom(OBJECTS));
        clause.setFeature(Feature.COMPLEMENTISER, chooseRandom(COMPLEMENTISERS));
        clause.setFeature(Feature.TENSE, Tense.FUTURE);

        if (randBool()) {
            clause.addComplement(chooseRandom(ADVERBS));
        }

        clause.setFeature(Feature.NEGATED, true);

        return Optional.ofNullable(REALISER.realiseSentence(clause));
    }
}
