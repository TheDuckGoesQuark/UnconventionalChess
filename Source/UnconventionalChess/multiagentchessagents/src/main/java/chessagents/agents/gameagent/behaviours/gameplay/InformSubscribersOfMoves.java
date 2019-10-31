package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameContext;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Move;
import chessagents.ontology.schemas.predicates.MoveMade;
import jade.content.abs.*;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.HashSet;
import java.util.Set;

import static jade.proto.SubscriptionResponder.Subscription;

/**
 *
 */
public class InformSubscribersOfMoves extends SimpleBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final AbsIRE absIRE = new AbsIRE(SLVocabulary.IOTA);
    private final Set<Subscription> subs = new HashSet<>();
    private final GameContext context;
    private int nextTurnIndex = 0;

    InformSubscribersOfMoves(GameContext context) {
        this.context = context;

        // construct IRE
        var absVariableMove = new AbsVariable("Move", ChessOntology.MOVE_MADE_MOVE);
        absIRE.setVariable(absVariableMove);

        var absProp = new AbsPredicate(ChessOntology.MOVE_MADE);
        absProp.set(ChessOntology.MOVE_MADE_MOVE, absVariableMove);
        absIRE.setProposition(absProp);
    }

    @Override
    public void action() {
        while (nextTurnIndex < context.getBoard().getTurnCount()) {
            try {
                sendNextTurn(nextTurnIndex);
            } catch (OntologyException e) {
                logger.warning("Failed to serialise move: " + e.getMessage());
            } finally {
                nextTurnIndex++;
            }
        }
    }

    private AbsContentElement constructMessageContent(Move move) throws OntologyException {
        var absMove = ChessOntology.getInstance().fromObject(move);
        var equals = new AbsPredicate(BasicOntology.EQUALS);

        equals.set(BasicOntology.EQUALS_LEFT, absIRE);
        equals.set(BasicOntology.EQUALS_RIGHT, absMove);

        return equals;
    }

    private void sendNextTurn(int indexOfNextTurn) throws OntologyException {
        var move = context.getBoard().getMove(indexOfNextTurn);
        var content = constructMessageContent(move);
        subs.stream().map(s -> createInform(s, content)).forEach(inform -> myAgent.send(inform));
    }

    private ACLMessage createInform(Subscription subscription, AbsContentElement content) {
        var reply = subscription.getMessage().createReply();

        try {
            myAgent.getContentManager().fillContent(reply, content);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to serialise inform message: " + e.getMessage());
        }

        return reply;
    }

    @Override
    public boolean done() {
        return context.getBoard().gameIsOver();
    }

    void addSubscriber(Subscription sub) {
        logger.info("New subscriber to moves: " + sub.getMessage().getSender());
        subs.add(sub);
    }

    void removeSubscriber(String conversationId) {
        subs.stream()
                .filter(s -> s.getMessage().getConversationId().equals(conversationId))
                .findFirst()
                .ifPresent(subs::remove);
    }
}
