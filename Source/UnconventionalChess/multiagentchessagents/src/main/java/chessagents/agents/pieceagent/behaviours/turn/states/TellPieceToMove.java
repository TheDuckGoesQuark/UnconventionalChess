package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.ChessAgent;
import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Move;
import chessagents.ontology.schemas.concepts.Piece;
import chessagents.ontology.schemas.concepts.Position;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.TOLD_PIECE_TO_MOVE;

public class TellPieceToMove extends SimpleBehaviour implements PieceStateBehaviour {
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public TellPieceToMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        pieceContext.getGameContext().getBoard().getRandomMove().ifPresent(move -> {
            var movingPiece = pieceContext.getGameContext().getPieceAtPosition(move.getSource());
            logger.info("Telling " + movingPiece.get().getAgentAID() + " to move");
            var request = createRequestToMove(movingPiece.get().getAgentAID(), move);
            sendRequestMove(request);
        });
    }

    private void sendRequestMove(ACLMessage request) {
        var aids = pieceContext.getGameContext().getAllPieceAgentAIDs();

        aids.forEach(request::addReceiver);
        aids.forEach(request::addReplyTo);

        myAgent.send(request);
    }

    private ACLMessage createRequestToMove(AID movingPiece, Move move) {
        var request = ChessMessageBuilder.constructMessage(ACLMessage.REQUEST);
        var makeMove = new MakeMove(move);
        var action = new Action(movingPiece, makeMove);

        try {
            myAgent.getContentManager().fillContent(request, action);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to create make move request: " + e.getMessage());
        }

        return request;
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int getNextTransition() {
        return TOLD_PIECE_TO_MOVE.ordinal();
    }

    @Override
    public int onEnd() {
        return getNextTransition();
    }
}
