package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.NoAction;
import chessagents.agents.pieceagent.TurnContext;
import chessagents.agents.pieceagent.actions.PerformMoveAction;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.actions.reactedtopreviousproposal.ReactToProposedMove;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PerformMove;
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
        var action = chooseAction();
        setChosenAction(action);
    }

    private PieceAction chooseAction() {
        if (turnContext.getCurrentMove() != null) {
            return new ReactToProposedMove(getMyPiece(), pieceContext.getGameState(), new PerformMoveAction(getMyPiece(), turnContext.getCurrentMove()));
        } else {
            return new NoAction(REACTED_TO_PREVIOUS_PROPOSAL, "React to previous proposal", getMyPiece());
        }
    }
}
