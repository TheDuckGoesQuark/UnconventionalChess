package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.REACTED_TO_PREVIOUS_PROPOSAL;

public class ReactToPreviousProposal extends PieceStateBehaviour {
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public ReactToPreviousProposal(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.REACT_TO_PREVIOUS_PROPOSAL);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        logger.info("Reacting to previous proposal");
        setEvent(REACTED_TO_PREVIOUS_PROPOSAL);
        // TODO react to previous proposal
    }
}
