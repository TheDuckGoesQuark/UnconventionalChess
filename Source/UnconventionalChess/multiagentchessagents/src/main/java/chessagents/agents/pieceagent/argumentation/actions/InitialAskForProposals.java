package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.TurnDiscussion;

public class InitialAskForProposals extends AskForProposals {
    public InitialAskForProposals(PieceAgent pieceAgent, TurnDiscussion turnDiscussion) {
        super(pieceAgent, turnDiscussion);
    }
}
