package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Move;
import chessagents.ontology.schemas.concepts.Piece;
import chessagents.ontology.schemas.concepts.Position;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.TOLD_PIECE_TO_MAKE_MOVE;

public class TellPieceToMakeMove extends SimpleBehaviour {
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public TellPieceToMakeMove(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        pieceContext.getBoard().getRandomMove().ifPresent(move -> {
            var movingPiece = getAIDOfPieceAtPosition(move.getSource());
            var request = createRequestToMove(movingPiece, move);
            sendRequestMove(request);
        });
    }

    private void sendRequestMove(ACLMessage request) {
        var pieces = pieceContext.getAidToPiece().values();

        pieces.stream().map(Piece::getAgentAID)
                .forEach(request::addReceiver);
        pieces.stream().map(Piece::getAgentAID)
                .forEach(request::addReplyTo);

        logger.info("Telling piece to make move");
        myAgent.send(request);
    }

    private ACLMessage createRequestToMove(AID movingPiece, Move move) {
        var request = new ACLMessage(ACLMessage.REQUEST);
        request.setOntology(ChessOntology.ONTOLOGY_NAME);
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        var makeMove = new MakeMove(move);
        var action = new Action(movingPiece, makeMove);

        try {
            myAgent.getContentManager().fillContent(request, action);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to create make move request: " + e.getMessage());
        }

        return request;
    }

    private AID getAIDOfPieceAtPosition(Position position) {
        for (Piece piece : pieceContext.getAidToPiece().values()) {
            if (piece.getPosition().equals(position)) {
                OntoAID agentAID = piece.getAgentAID();
                return agentAID;
            }
        }
        return null;
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public int onEnd() {
        return TOLD_PIECE_TO_MAKE_MOVE.ordinal();
    }
}
