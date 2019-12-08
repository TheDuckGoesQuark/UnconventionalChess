package chessagents.agents.pieceagent.actions;


import chessagents.agents.pieceagent.PieceContext;
import chessagents.chess.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.lang.acl.ACLMessage;
import simplenlg.features.Feature;
import simplenlg.features.Tense;

import java.util.List;
import java.util.Optional;

import static chessagents.agents.pieceagent.nlg.NLGUtil.NLG_FACTORY;
import static chessagents.agents.pieceagent.nlg.NLGUtil.REALISER;

public class AgreeToMoveAction extends PieceAction {

    private static final List<String> OBJECTS = List.of("there", "position");
    private static final List<String> VERB = List.of("move", "slide", "jump", "walk", "shift", "run");
    private static final List<String> COMPLEMENTISERS = List.of("to", "that");

    private final ACLMessage requestToMove;
    private final PieceMove requestedMove;

    /**
     * @param actor         piece performing the action
     * @param requestToMove request to move message
     * @param requestedMove move that has been requested be made
     */
    public AgreeToMoveAction(ChessPiece actor, ACLMessage requestToMove, PieceMove requestedMove) {
        super(PieceTransition.AGREED_TO_MAKE_MOVE, "Agree to move", actor, true);
        this.requestToMove = requestToMove;
        this.requestedMove = requestedMove;
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        sendAgree(actor, requestToMove);
        return gameState;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState.makeMove(requestedMove);
    }

    private void sendAgree(PieceAgent actor, ACLMessage message) {
        var agree = message.createReply();
        agree.setPerformative(ACLMessage.AGREE);
        agree.removeReceiver(actor.getAID());
        actor.send(agree);
    }

    @Override
    public Optional<String> verbalise(PieceContext context) {
        var clause = NLG_FACTORY.createClause();

        clause.setSubject("I");
        clause.setVerb(chooseRandom(VERB));
        clause.setObject(chooseRandom(OBJECTS));
        clause.setFeature(Feature.COMPLEMENTISER, chooseRandom(COMPLEMENTISERS));
        clause.setFeature(Feature.TENSE, Tense.FUTURE);

        return Optional.ofNullable(REALISER.realiseSentence(clause));
    }
}
