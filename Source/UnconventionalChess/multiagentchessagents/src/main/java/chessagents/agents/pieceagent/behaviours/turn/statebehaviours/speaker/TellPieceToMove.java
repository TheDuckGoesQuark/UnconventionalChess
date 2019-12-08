package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.actions.TellPieceToMoveAction;

import java.util.Set;
import java.util.stream.Collectors;

public class TellPieceToMove extends PieceStateBehaviour {
    private final TurnContext turnContext;

    public TellPieceToMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.TELL_PIECE_TO_MOVE);
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        setChosenAction(getAgent().chooseAction(generatePossibleActions()));
    }

    private Set<PieceAction> generatePossibleActions() {
        var me = getMyPiece();

        // generate all possible future moves that we can try make happen
        return pieceContext.getGameState().getAllLegalMoves().stream()
                .map(move -> new TellPieceToMoveAction(me, move, pieceContext.getGameState().getPieceAtPosition(move.getSource()).get()))
                .collect(Collectors.toSet());
    }

}
