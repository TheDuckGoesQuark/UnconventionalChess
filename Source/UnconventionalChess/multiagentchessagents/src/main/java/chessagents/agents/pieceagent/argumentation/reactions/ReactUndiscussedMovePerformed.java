package chessagents.agents.pieceagent.argumentation.reactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationAction;
import chessagents.agents.pieceagent.argumentation.TurnDiscussion;

public class ReactUndiscussedMovePerformed extends ReactLastMoveDiscussedPerformed{
    public ReactUndiscussedMovePerformed(PieceAgent pieceAgent) {
        super(pieceAgent);
    }
}
