package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.events.ToldPieceToMoveEvent;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.planner.PieceAction;
import chessagents.agents.pieceagent.planner.actions.TellPieceToMoveAction;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

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
        var me = pieceContext.getPieceForAID(myAgent.getAID()).get();

        return pieceContext.getGameState().getAllLegalMoves().stream()
                .map(move -> new TellPieceToMoveAction(me, move, pieceContext.getGameState().getPieceAtPosition(move.getSource()).get()))
                .collect(Collectors.toSet());
    }

}
