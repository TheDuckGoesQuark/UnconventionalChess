package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.NoAction;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.REACTED_TO_PREVIOUS_PROPOSAL;

public class ReactToPreviousProposal extends PieceStateBehaviour {
    private final TurnContext turnContext;

    public ReactToPreviousProposal(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.REACT_TO_PREVIOUS_PROPOSAL);
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        // TODO react?
        setChosenAction(new NoAction(REACTED_TO_PREVIOUS_PROPOSAL, "React to previous proposal", getMyPiece()));
    }
}
