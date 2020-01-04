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

public class ReactFriendlyPieceThreatenedOnOurMove extends ReactFriendlyPieceThreatened{
    public ReactFriendlyPieceThreatenedOnOurMove(PieceAgent pieceAgent) {
        super(pieceAgent);
    }
}
