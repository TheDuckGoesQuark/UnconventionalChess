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
public class ReactFriendlyPieceEscaped extends ConversationAction {

    private final PieceAgent pieceAgent;

    @Override
    public ConversationMessage perform() {
        var pieceContext = pieceAgent.getPieceContext();
        var gameHistory = pieceContext.getGameHistory();
        var gameStateBeforeMove = gameHistory.getPreviousState();
        var gameStateAfterMove = gameHistory.getCurrentState();
        var movePerformed = gameHistory.getLastMove();

        var escapingPiece = getEscapingPiece(gameStateBeforeMove, gameStateAfterMove);
        var escapingPieceName = escapingPiece.getAgentAID().getLocalName();
        var movingPiece = gameStateBeforeMove.getPieceAtPosition(movePerformed.getSource()).get();
        var movingPieceName = movingPiece.getAgentAID().getLocalName();


        // we dont want to always call out protective pieces, so 50/50 it
        if (!escapingPiece.equals(movingPiece) && RandomUtil.randBool()) {
            return new ReactFriendlyPieceProtecting(pieceAgent, movingPieceName, escapingPieceName).perform();
        }

        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setEscapingPiece(escapingPieceName);
        grammarVariableProvider.setMovingPiece(movingPieceName);

        var personality = pieceAgent.getPieceContext().getPersonality();
        var traitResponsible = new RandomUtil<Trait>().chooseRandom(personality.getTraits());
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), pieceAgent.getAID());
    }

    private ChessPiece getEscapingPiece(GameState gameStateBeforeMove, GameState gameStateAfterMove) {
        var ourColour = pieceAgent.getPieceContext().getMyPiece().getColour();
        var threatenedBefore = gameStateBeforeMove.getThreatenedForColour(ourColour);
        var threatenedAfter = gameStateAfterMove.getThreatenedForColour(ourColour);

        return threatenedBefore.stream()
                .filter(p -> !threatenedAfter.contains(p))
                .findFirst().get();
    }
}
