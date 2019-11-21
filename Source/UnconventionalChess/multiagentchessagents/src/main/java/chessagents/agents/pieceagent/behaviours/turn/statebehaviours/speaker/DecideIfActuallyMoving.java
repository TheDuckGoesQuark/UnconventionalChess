package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.AgreeToMoveAction;
import chessagents.agents.pieceagent.actions.FalselyAgreeToMoveAction;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.actions.RefuseToMoveAction;
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
        var pieceAgent = getAgent();

        setChosenAction(getAgent().chooseAction(generatePossibleActions(message, turnContext.getCurrentMove())));

        if (pieceAgent.isActuallyMoving()) {
            // Give piece opportunity to actually perform different move
            // TODO commented out until able to fetch next move from plan
//            turnContext.setCurrentMove(pieceAgent.getNextMove());
            setEvent(ACTUALLY_MOVING);
        } else {
            // TODO send failure to everyone if not actually moving
            setEvent(FAILED_TO_MOVE);
        }
    }

    private Set<PieceAction> generatePossibleActions(ACLMessage requestToMove, PieceMove currentMove) {
        var myPiece = getMyPiece();

        var agreeToMove = new AgreeToMoveAction(myPiece, requestToMove, currentMove);
        var falslyAgreeToMove = new FalselyAgreeToMoveAction(myPiece, requestToMove, currentMove);
        // TODO agree but perform different move?
        var refuseToMove = new RefuseToMoveAction(myPiece);

        return new HashSet<>(Arrays.asList(agreeToMove, falslyAgreeToMove, refuseToMove));
    }
}
