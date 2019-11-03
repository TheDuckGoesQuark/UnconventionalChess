package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.concepts.Piece;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.REACTED_TO_PREVIOUS_PROPOSAL;

public class ReactToPreviousProposal extends OneShotBehaviour implements PieceStateBehaviour {
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public ReactToPreviousProposal(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public int getNextTransition() {
        return REACTED_TO_PREVIOUS_PROPOSAL.ordinal();
    }

    @Override
    public void action() {
        // TODO
    }
}
