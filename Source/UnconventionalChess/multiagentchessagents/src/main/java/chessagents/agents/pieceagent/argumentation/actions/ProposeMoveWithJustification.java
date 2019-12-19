package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.TurnDiscussion;

public class ProposeMoveWithJustification extends ProposeMove {
    public ProposeMoveWithJustification(PieceAgent pieceAgent, TurnDiscussion turnDiscussion) {
        super(pieceAgent, turnDiscussion);
    }
}
