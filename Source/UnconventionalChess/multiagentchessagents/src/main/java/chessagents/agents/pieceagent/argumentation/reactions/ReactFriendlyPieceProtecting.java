package chessagents.agents.pieceagent.argumentation.reactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationAction;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.GrammarVariableProviderImpl;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;

public class ReactFriendlyPieceProtecting extends ConversationAction {
    private final PieceAgent pieceAgent;
    private final String protectorPieceName;
    private final String protectedPieceName;

    public ReactFriendlyPieceProtecting(PieceAgent pieceAgent, String protectorPieceName, String protectedPieceName) {
        this.pieceAgent = pieceAgent;
        this.protectorPieceName = protectorPieceName;
        this.protectedPieceName = protectedPieceName;
    }

    @Override
    public ConversationMessage perform() {
        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setEscapingPiece(protectedPieceName);
        grammarVariableProvider.setMovingPiece(protectorPieceName);

        var personality = pieceAgent.getPieceContext().getPersonality();
        var traitResponsible = new RandomUtil<Trait>().chooseRandom(personality.getTraits());
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), pieceAgent.getAID());
    }
}
