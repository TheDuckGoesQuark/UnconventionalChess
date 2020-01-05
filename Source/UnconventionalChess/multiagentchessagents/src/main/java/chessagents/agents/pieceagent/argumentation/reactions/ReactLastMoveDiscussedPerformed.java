package chessagents.agents.pieceagent.argumentation.reactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.*;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.util.RandomUtil;

import java.util.Set;

public class ReactLastMoveDiscussedPerformed extends ConversationAction {

    private final PieceAgent pieceAgent;
    private MoveResponse responseToLastMove;

    public ReactLastMoveDiscussedPerformed(PieceAgent pieceAgent) {
        this.pieceAgent = pieceAgent;
    }

    @Override
    protected String grammarTag() {
        return "<" + getClass().getSimpleName() + responseToLastMove.getOpinion().name() + ">";
    }

    @Override
    public ConversationMessage perform() {
        var moveMade = getMoveMade();
        var previousGameState = getPreviousGameState();
        var pieceContext = pieceAgent.getPieceContext();

        // choose random from our responses to this move
        var responses = pieceContext.getPersonality().getResponseToMoves(pieceContext.getMyPiece(), Set.of(moveMade), previousGameState);
        responseToLastMove = new RandomUtil<MoveResponse>().chooseRandom(responses);

        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setMoveResponse(responseToLastMove);
        grammarVariableProvider.setMovingPiece(getMovingPiece(responseToLastMove, pieceAgent));

        var personality = pieceAgent.getPieceContext().getPersonality();
        var traitResponsible = new RandomUtil<Trait>().chooseRandom(personality.getTraits());
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), pieceAgent.getAID());
    }

    private GameState getPreviousGameState() {
        return pieceAgent.getPieceContext().getGameHistory().getPreviousState();
    }

    private PieceMove getMoveMade() {
        return pieceAgent.getPieceContext().getGameHistory().getLastMove();
    }
}
