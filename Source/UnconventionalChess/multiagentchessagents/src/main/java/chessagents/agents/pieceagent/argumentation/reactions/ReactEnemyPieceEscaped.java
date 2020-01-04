package chessagents.agents.pieceagent.argumentation.reactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationAction;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.GrammarVariableProviderImpl;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReactEnemyPieceEscaped extends ConversationAction {

    private final PieceAgent pieceAgent;

    @Override
    public ConversationMessage perform() {
        var pieceContext = pieceAgent.getPieceContext();
        var gameHistory = pieceContext.getGameHistory();
        var gameStateBeforeMove = gameHistory.getPreviousState();
        var gameStateAfterMove = gameHistory.getCurrentState();
        var movePerformed = gameHistory.getLastMove();

        var escapingPiece = getEscapingPiece(gameStateBeforeMove, gameStateAfterMove);
        var movingPiece = gameStateBeforeMove.getPieceAtPosition(movePerformed.getSource()).get();

        // we dont want to always call out protective pieces, so 50/50 it
        if (!escapingPiece.equals(movingPiece) && RandomUtil.randBool()) {
            return new ReactEnemyPieceProtecting(pieceAgent, movingPiece.getType(), escapingPiece.getType()).perform();
        }

        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setEscapingPiece(escapingPiece.getType());
        grammarVariableProvider.setMovingPiece(movingPiece.getType());

        var personality = pieceAgent.getPieceContext().getPersonality();
        var traitResponsible = new RandomUtil<Trait>().chooseRandom(personality.getTraits());
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), pieceAgent.getAID());
    }

    private ChessPiece getEscapingPiece(GameState gameStateBeforeMove, GameState gameStateAfterMove) {
        var otherColour = pieceAgent.getPieceContext().getMyPiece().getColour().flip();
        var threatenedBefore = gameStateBeforeMove.getThreatenedForColour(otherColour);
        var threatenedAfter = gameStateAfterMove.getThreatenedForColour(otherColour);

        return threatenedBefore.stream()
                .filter(p -> !threatenedAfter.contains(p))
                .findFirst().get();
    }
}
