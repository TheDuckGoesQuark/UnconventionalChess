package chessagents.agents.gatewayagent.behaviours;

import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Move;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Optional;

import static chessagents.agents.gameagent.behaviours.gameplay.HandleMoveSubscriptions.MOVE_SUBSCRIPTION_PROTOCOL;

public class CallbackOnMove extends CyclicBehaviour {

    // TODO ONLY WORKS FOR ONE GAME
    private MessageHandler handler;

    public CallbackOnMove(MessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void action() {
        var message = myAgent.receive(MessageTemplate.and(
                MessageTemplate.MatchProtocol(MOVE_SUBSCRIPTION_PROTOCOL),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF))
        );

        if (message != null) {
            var move = extractMove(message);
            handler.setMove(move.get());
            handler.setAgentID(message.getSender());
            handler.call();
        }
    }

    private Optional<Move> extractMove(ACLMessage message) {
        Optional<Move> result = Optional.empty();
        try {
            var absEquals = (AbsPredicate) myAgent.getContentManager().extractAbsContent(message);

            if (!absEquals.getTypeName().equals(BasicOntology.EQUALS)) {
                throw new NotUnderstoodException("Did not receive expected equals predicate");
            }

            var absRight = absEquals.getAbsTerm(BasicOntology.EQUALS_RIGHT);
            var move = (Move) ChessOntology.getInstance().toObject(absRight);
            result = Optional.of(move);
        } catch (Codec.CodecException | OntologyException | NotUnderstoodException e) {
            System.out.println("BAD: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
