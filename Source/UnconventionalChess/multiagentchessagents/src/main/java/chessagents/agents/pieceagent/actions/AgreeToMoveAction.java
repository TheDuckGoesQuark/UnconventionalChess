package chessagents.agents.pieceagent.actions;


import chessagents.agents.pieceagent.PieceContext;
import chessagents.chess.GameState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.play.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.util.RandomUtil;
import jade.lang.acl.ACLMessage;

import java.util.List;
import java.util.Optional;

public class AgreeToMoveAction extends PieceAction {

    private static final List<String> AGREE = List.of("sure!", "ok", "okay", "If I have to...", "If you say so.", "Here we go...");

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
        return Optional.ofNullable(new RandomUtil<String>().chooseRandom(AGREE));
    }
}
