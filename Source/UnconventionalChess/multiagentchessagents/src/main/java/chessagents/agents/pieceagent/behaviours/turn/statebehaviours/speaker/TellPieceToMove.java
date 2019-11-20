package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.actions.TellPieceToMoveAction;

import java.util.Set;
import java.util.stream.Collectors;

public class TellPieceToMove extends PieceStateBehaviour {
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public TellPieceToMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.TELL_PIECE_TO_MOVE);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        setChosenAction(pieceContext.chooseAction(generatePossibleActions()));
    }

    private Set<PieceAction> generatePossibleActions() {
        var me = getMyPiece();

        return pieceContext.getGameState().getAllLegalMoves().stream()
                .map(move -> new TellPieceToMoveAction(me, move, pieceContext.getGameState().getPieceAtPosition(move.getSource()).get()))
                .collect(Collectors.toSet());
    }

}
