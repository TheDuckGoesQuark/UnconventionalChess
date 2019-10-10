package chessagents.agents.gameagent.behaviours;

import chessagents.ontology.schemas.concepts.Move;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Constantly responds to requests for available moves
 */
public class InformPossibleMoves extends CyclicBehaviour {

    private Board board;
    private ACLMessage reply;
    private MessageTemplate template =
            MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);

    public InformPossibleMoves(Board board) {
        this.board = board;
    }

    @Override
    public void action() {
        ACLMessage message = myAgent.receive(template);

        if (message != null) {
            try {
                reply(message);
            } catch (MoveGeneratorException | Codec.CodecException | OntologyException e) {
                e.printStackTrace();
            }
        }

        block();
    }

    /**
     * Reply to agent with all currently legal moves that can be made
     *
     * @param message
     * @throws MoveGeneratorException
     * @throws Codec.CodecException
     * @throws OntologyException
     */
    private void reply(ACLMessage message) throws MoveGeneratorException, Codec.CodecException, OntologyException {
        reply = message.createReply();
        reply.setPerformative(ACLMessage.INFORM);

        List<Move> moves = MoveGenerator.generateLegalMoves(board)
                .stream()
                .map(move -> new Move(move.getFrom().toString(), move.getTo().toString()))
                .collect(Collectors.toList());

        MoveList moveList = new MoveList();
        moveList.setMoves(moves);

        Action request = (Action) myAgent.getContentManager().extractContent(message);
        myAgent.getContentManager().fillContent(reply, new Result(request.getAction(), moveList));

        myAgent.send(reply);
    }
}
