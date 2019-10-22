package chessagents.agents.pieceagent.behaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Move;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.Optional;
import java.util.Set;

import static chessagents.agents.pieceagent.PieceAgent.*;

// TODO
public class Play extends CyclicBehaviour {

    private final BoardWrapper board;
    private final Colour myColour;
    private final Set<AID> pieceAgents;
    private final AID gameAgentAid = new AID("GameAgent-0", false);

    private boolean moveSent = false;

    public Play(PieceAgent pieceAgent, DataStore dataStore) {
        this.board = (BoardWrapper) dataStore.get(BOARD_KEY);
        this.myColour = (Colour) dataStore.get(MY_COLOUR_KEY);
        this.pieceAgents = (Set<AID>) dataStore.get(PIECE_AID_SET_KEY);
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
