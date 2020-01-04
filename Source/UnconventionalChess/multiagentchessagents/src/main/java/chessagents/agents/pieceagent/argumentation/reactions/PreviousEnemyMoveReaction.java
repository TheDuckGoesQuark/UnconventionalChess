package chessagents.agents.pieceagent.argumentation.reactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationAction;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.GrammarVariableProviderImpl;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PreviousEnemyMoveReaction extends ConversationAction {

    private final PieceAgent pieceAgent;

    @Override
    public ConversationMessage perform() {
        var pieceContext = pieceAgent.getPieceContext();
        var move = pieceContext.getGameHistory().getLastMove();
        var movingPiece = pieceContext.getGameState().getPieceAtPosition(move.getTarget()).get();

        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setMovingPiece(movingPiece.getType());

        var personality = pieceContext.getPersonality();
        var traitResponsible = new RandomUtil<Trait>().chooseRandom(personality.getTraits());
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), pieceAgent.getAID());
    }
}
