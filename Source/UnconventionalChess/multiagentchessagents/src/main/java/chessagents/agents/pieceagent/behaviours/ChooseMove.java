package chessagents.agents.pieceagent.behaviours;

import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Colour;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class ChooseMove extends CyclicBehaviour {

    private Board board;
    private Random random = new Random();
    private Colour myColour;
    private AID gameAgentAid = new AID("GameAgent-0", false);

    public ChooseMove(Board board, Colour myColour) {
        this.board = board;
        this.myColour = myColour;
    }

    @Override
    public void action() {
        if (board.getSideToMove().value().equals(myColour.getColour())) {
            sendRandomMove();
        }

        receiveMove();
        block();
    }

    private void receiveMove() {
        ACLMessage message = myAgent.receive();

        if (message == null) return;

        if (message.getPerformative() == ACLMessage.INFORM) {
            try {
                MakeMove move = (MakeMove) ((Action) myAgent.getContentManager().extractContent(message)).getAction();
                board.doMove(new Move(
                        Square.valueOf(move.getMove().getSource().getCoordinates()),
                        Square.valueOf(move.getMove().getTarget().getCoordinates()))
                );
            } catch (Codec.CodecException | OntologyException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRandomMove() {
        try {
            Move move = MoveGenerator.generateLegalMoves(board).get(random.nextInt());

            MakeMove makeMoveAction = new MakeMove();
            makeMoveAction.setMove(new chessagents.ontology.schemas.concepts.Move(move.getFrom().toString(), move.getTo().toString()));

            ACLMessage moveMessage = new ACLMessage(ACLMessage.PROPOSE);
            moveMessage.addReceiver(gameAgentAid);
            myAgent.getContentManager().fillContent(moveMessage, makeMoveAction);

        } catch (MoveGeneratorException | Codec.CodecException | OntologyException e) {
            e.printStackTrace();
        }
    }
}
