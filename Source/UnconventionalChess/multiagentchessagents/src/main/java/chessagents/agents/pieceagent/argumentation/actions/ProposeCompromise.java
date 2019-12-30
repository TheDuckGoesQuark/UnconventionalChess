package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.Opinion;
import chessagents.agents.pieceagent.argumentation.TurnDiscussion;
import chessagents.agents.pieceagent.personality.values.Value;
import chessagents.ontology.schemas.concepts.PieceMove;

import java.util.Set;
import java.util.stream.Collectors;

public class ProposeCompromise extends ProposeMove {
    private final Value necessaryValue;

    public ProposeCompromise(PieceAgent pieceAgent, TurnDiscussion turnDiscussion, Value necessaryValue) {
        super(pieceAgent, turnDiscussion);
        this.necessaryValue = necessaryValue;
    }

    @Override
    protected Set<PieceMove> getProposableMoves() {
        var pieceContext = getPieceAgent().getPieceContext();
        var myPiece = pieceContext.getMyPiece();
        var gameState = pieceContext.getGameState();

        // filter moves that meet the necessary value and haven't been already proposed
        var proposableMoves = gameState.getAllLegalMoves().stream()
                .map(m -> necessaryValue.getMoveResponse(myPiece, gameState, m))
                .filter(r -> r.getOpinion() == Opinion.LIKE)
                .map(m -> m.getMove().get())
                .collect(Collectors.toSet());

        // remove previously discussed moves
        getTurnDiscussion().getPreviouslyDiscussedMoves().forEach(proposableMoves::remove);

        return proposableMoves;
    }
}
