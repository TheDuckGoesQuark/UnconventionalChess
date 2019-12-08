package chessagents.agents.pieceagent.actions.proposalsrequested;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.TurnContext;
import chessagents.ontology.schemas.concepts.ChessPiece;

import java.util.Optional;

public class LetOtherPieceSuggest extends AskOtherPiecesForIdeas {
    public LetOtherPieceSuggest(ChessPiece actor, TurnContext turnContext) {
        super(actor, turnContext);
    }

    @Override
    public Optional<String> verbalise(PieceContext context) {
        return Optional.empty();
    }
}
