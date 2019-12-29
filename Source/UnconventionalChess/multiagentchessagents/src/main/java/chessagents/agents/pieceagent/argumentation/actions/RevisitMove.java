package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.TurnDiscussion;
import chessagents.ontology.schemas.concepts.PieceMove;

import java.util.HashSet;
import java.util.Set;

public class RevisitMove extends ProposeMove {
    public RevisitMove(PieceAgent pieceAgent, TurnDiscussion turnDiscussion) {
        super(pieceAgent, turnDiscussion);
    }

    @Override
    protected Set<PieceMove> getProposableMoves() {
        var possibleMoves = getTurnDiscussion().getPreviouslyDiscussedMoves();

        // remove last discussed move cause that would be silly
        possibleMoves.remove(possibleMoves.size() - 1);

        return new HashSet<>(possibleMoves);
    }
}
