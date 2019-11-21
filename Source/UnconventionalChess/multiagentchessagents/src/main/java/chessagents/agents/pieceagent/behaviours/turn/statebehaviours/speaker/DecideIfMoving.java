package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.AgreeToMoveAction;
import chessagents.agents.pieceagent.actions.FalselyAgreeToMoveAction;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.actions.RefuseToMoveAction;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.AGREED_TO_MAKE_MOVE;
import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.NOT_MOVING;

public class DecideIfMoving extends PieceStateBehaviour {

    private final TurnContext turnContext;

    public DecideIfMoving(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.DECIDE_IF_MOVING);
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var message = turnContext.getCurrentMessage();
        saveMove(message);
        setChosenAction(getAgent().chooseAction(generatePossibleActions(message, turnContext.getCurrentMove())));
    }

    private Set<PieceAction> generatePossibleActions(ACLMessage requestToMove, PieceMove currentMove) {
        var myPiece = getMyPiece();

        var agreeToMove = new AgreeToMoveAction(myPiece, requestToMove, currentMove);
        var falslyAgreeToMove = new FalselyAgreeToMoveAction(myPiece, requestToMove, currentMove);
        // TODO agree but perform different move?
        var refuseToMove = new RefuseToMoveAction(myPiece);

        return new HashSet<>(Arrays.asList(agreeToMove, falslyAgreeToMove, refuseToMove));
    }

    private void saveMove(ACLMessage message) {
        try {
            var action = (Action) myAgent.getContentManager().extractContent(message);
            var makeMove = (MakeMove) action.getAction();
            turnContext.setCurrentMove(makeMove.getMove());
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed " + e.getMessage());
        }
    }
}
