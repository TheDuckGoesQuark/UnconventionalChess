package chessagents.agents.pieceagent.argumentation.reactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationAction;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.GrammarVariableProviderImpl;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReactEnemyPieceCaptured extends ConversationAction {

    private final PieceAgent pieceAgent;

    @Override
    public ConversationMessage perform() {
        var gameHistory = pieceAgent.getPieceContext().getGameHistory();
        var gameStateBeforeMove = gameHistory.getPreviousState();
        var movePerformed = gameHistory.getLastMove();

        var capturedPiece = gameStateBeforeMove.getPieceAtPosition(movePerformed.getTarget()).get();
        var capturingPiece = gameStateBeforeMove.getPieceAtPosition(movePerformed.getSource()).get();

        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setCapturedPiece(capturedPiece.getType());
        grammarVariableProvider.setMovingPiece(capturingPiece.getAgentAID().getLocalName());
        var personality = pieceAgent.getPieceContext().getPersonality();
        var traitResponsible = new RandomUtil<Trait>().chooseRandom(personality.getTraits());
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), pieceAgent.getAID());
    }
}
