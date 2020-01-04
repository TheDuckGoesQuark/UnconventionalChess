package chessagents.agents.pieceagent.argumentation.reactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationAction;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.GrammarVariableProviderImpl;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;

public class ReactEnemyPieceProtecting extends ConversationAction {

    private final PieceAgent pieceAgent;
    private final String protectorPieceType;
    private final String protectedPieceType;

    public ReactEnemyPieceProtecting(PieceAgent pieceAgent, String protectorPieceType, String protectedPieceType) {
        this.pieceAgent = pieceAgent;
        this.protectorPieceType = protectorPieceType;
        this.protectedPieceType = protectedPieceType;
    }

    @Override
    public ConversationMessage perform() {
        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setEscapingPiece(protectedPieceType);
        grammarVariableProvider.setMovingPiece(protectorPieceType);

        var personality = pieceAgent.getPieceContext().getPersonality();
        var traitResponsible = new RandomUtil<Trait>().chooseRandom(personality.getTraits());
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), pieceAgent.getAID());
    }
}
