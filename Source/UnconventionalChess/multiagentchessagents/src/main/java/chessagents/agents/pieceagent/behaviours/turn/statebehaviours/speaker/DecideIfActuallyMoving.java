package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.*;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.ACTUALLY_MOVING;
import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.FAILED_TO_MOVE;

public class DecideIfActuallyMoving extends PieceStateBehaviour {

    private final TurnContext turnContext;

    public DecideIfActuallyMoving(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.DECIDE_IF_ACTUALLY_MOVING);
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        setChosenAction(getAgent().chooseAction(generatePossibleActions()));
    }

    private Set<PieceAction> generatePossibleActions() {
        var myPiece = getMyPiece();

        // TODO add performing any other move as option too because why not
        var performRequestedMove = new NoAction(ACTUALLY_MOVING, "Perform agreed upon move", myPiece);
        // TODO send failure to everyone if not actually moving and add not moving as option

        return new HashSet<>(Arrays.asList(performRequestedMove));
    }
}
