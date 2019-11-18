package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.planner.PieceAction;
import chessagents.agents.pieceagent.planner.actions.AskWhatMoveWeShouldDoAction;
import chessagents.agents.pieceagent.planner.actions.TellPieceToMoveAction;
import chessagents.ontology.schemas.concepts.ChessPiece;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DecideIfRequestingProposals extends PieceStateBehaviour {

    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public DecideIfRequestingProposals(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.DECIDE_IF_REQUESTING_PROPOSALS);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        setChosenAction(pieceContext.chooseAction(createPossibleActions(turnContext.getDebateCycles() < pieceContext.getMaxDebateCycle())));
    }

    private Set<PieceAction> createPossibleActions(boolean canDebateFurther) {
        var possibleActions = new HashSet<PieceAction>();

        var me = pieceContext.getPieceForAID(myAgent.getAID()).get();

        if (canDebateFurther) {
            // we could ask everyone what move we should make
            possibleActions.add(new AskWhatMoveWeShouldDoAction(me));
        }

        // we could tell any of the other pieces to move (or oneself!)
        possibleActions.addAll(generateActionForAllMoves(me));

        return possibleActions;
    }

    private Set<PieceAction> generateActionForAllMoves(ChessPiece me) {
        return pieceContext.getGameState().getAllLegalMoves().stream()
                .map(move -> new TellPieceToMoveAction(me, move, pieceContext.getGameState().getPieceAtPosition(move.getSource()).get()))
                .collect(Collectors.toSet());
    }
}
