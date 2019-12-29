package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.GrammarVariableProviderImpl;
import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.argumentation.TurnDiscussion;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

import java.util.Set;

public class ProposeCompromise extends ProposeMove {
    public ProposeCompromise(PieceAgent pieceAgent, TurnDiscussion turnDiscussion) {
        super(pieceAgent, turnDiscussion);
    }

    @Override
    protected Set<PieceMove> getProposableMoves() {
        // TODO get set of moves that agree with previous value
        return super.getProposableMoves();
    }
}
