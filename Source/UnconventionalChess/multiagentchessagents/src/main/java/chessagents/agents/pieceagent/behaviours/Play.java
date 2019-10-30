package chessagents.agents.pieceagent.behaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Move;
import chessagents.ontology.schemas.concepts.Piece;
import com.github.bhlangonijr.chesslib.Board;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static chessagents.agents.pieceagent.PieceAgent.*;

public class Play extends CyclicBehaviour {

    private final BoardWrapper board;
    private final Colour myColour;
    private final Map<AID, Piece> pieceAgents;
    private final AID gameAgentAid;

    private boolean moveSent = false;

    public Play(PieceAgent pieceAgent, Colour colour, Map<AID, Piece> pieceAgents, AID gameAgentAID) {
        super(pieceAgent);
        this.board = new BoardWrapper();
        this.myColour = colour;
        this.pieceAgents = pieceAgents;
        this.gameAgentAid = gameAgentAID;
    }

    private boolean myTurn() {
        return board.isSideToGo(myColour.getColour());
    }

    @Override
    public void action() {
        if (myTurn()) {
            if (moveSent) {
                receiveMove();
            } else {
                // wait for choice of move to come back
                sendRandomMove();
            }
        } else {
            // wait for other side to move
            receiveMove();
            moveSent = false;
        }

        block();
    }

    private void receiveMove() {
        ACLMessage message = myAgent.receive();

        if (message == null) return;

        if (message.getPerformative() == ACLMessage.INFORM) {
            try {
                MakeMove move = (MakeMove) ((Action) myAgent.getContentManager().extractContent(message)).getAction();
                board.makeMove(move.getMove().getSource().getCoordinates(), move.getMove().getTarget().getCoordinates());

                // prepare to make another move
                if (myTurn()) moveSent = false;
            } catch (Codec.CodecException | OntologyException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRandomMove() {
        try {
            final Optional<Move> move = board.getRandomMove();

            MakeMove makeMoveAction = new MakeMove();
            makeMoveAction.setMove(move.get());

            ACLMessage moveMessage = new ACLMessage(ACLMessage.PROPOSE);
            moveMessage.addReceiver(gameAgentAid);
            moveMessage.setOntology(ChessOntology.ONTOLOGY_NAME);
            moveMessage.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
            myAgent.getContentManager().fillContent(moveMessage, makeMoveAction);
            moveSent = true;
        } catch (Codec.CodecException | OntologyException e) {
            e.printStackTrace();
        }
    }
}
