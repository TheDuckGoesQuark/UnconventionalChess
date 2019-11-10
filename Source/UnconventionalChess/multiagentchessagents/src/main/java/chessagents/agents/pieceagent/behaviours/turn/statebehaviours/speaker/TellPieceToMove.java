package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.events.ToldPieceToMoveEvent;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Move;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.TOLD_PIECE_TO_MOVE;

public class TellPieceToMove extends PieceStateBehaviour {
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public TellPieceToMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.TELL_PIECE_TO_MOVE);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        pieceContext.getGameContext().getBoard().getRandomMove().ifPresent(move -> {
            var movingPiece = pieceContext.getGameContext().getPieceAtPosition(move.getSource()).get();
            logger.info("Telling " + movingPiece.getAgentAID() + " to move");
            var request = createRequestToMove(movingPiece.getAgentAID(), move);
            sendRequestMove(request);
            setEvent(new ToldPieceToMoveEvent(movingPiece, move));
        });
    }

    private void sendRequestMove(ACLMessage request) {
        var aids = pieceContext.getGameContext().getAllPieceAgentAIDs();

        // send to everyone so they know not to expect a CFP to speak
        aids.forEach(request::addReceiver);

        // tell actual recipient that they should also let everyone know of their response
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
}
