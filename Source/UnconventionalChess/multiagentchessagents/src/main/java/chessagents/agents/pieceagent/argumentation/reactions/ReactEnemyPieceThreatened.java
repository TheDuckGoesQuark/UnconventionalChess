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
public class ReactEnemyPieceThreatened extends ConversationAction {

    private final RandomUtil<ChessPiece> randomChooser = new RandomUtil<>();
    private final PieceAgent pieceAgent;

    @Override
    public ConversationMessage perform() {
        var gameState = pieceAgent.getPieceContext().getGameState();
        var threatenedPiece = getThreatenedPiece(gameState);

        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setThreatenedPiece(threatenedPiece.getType());

        var personality = pieceAgent.getPieceContext().getPersonality();
        var traitResponsible = new RandomUtil<Trait>().chooseRandom(personality.getTraits());
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), pieceAgent.getAID());
    }

    private ChessPiece getThreatenedPiece(GameState gameState) {
        var otherColour = pieceAgent.getPieceContext().getMyPiece().getColour().flip();
        return randomChooser.chooseRandom(gameState.getThreatenedForColour(otherColour));
    }
}
