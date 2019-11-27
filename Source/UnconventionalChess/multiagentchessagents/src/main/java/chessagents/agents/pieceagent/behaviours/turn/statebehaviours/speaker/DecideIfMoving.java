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

    private Set<PieceAction> generatePossibleActions(ACLMessage requestToMove, PieceMove requestedMove) {
        var myPiece = getMyPiece();
        var actions = new HashSet<PieceAction>();

        actions.add(new AgreeToMoveAction(myPiece, requestToMove, requestedMove));
        actions.add(new FalselyAgreeToMoveAction(myPiece, requestToMove, requestedMove));

        // Only allow rejection to occur if more debate cycles are allowed, otherwise we could go on forever...
        if (turnContext.getDebateCycles() < pieceContext.getMaxDebateCycle())
            actions.add(new RefuseToMoveAction(myPiece, requestToMove, requestedMove));

        // TODO agree but perform different move?

        return actions;
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
