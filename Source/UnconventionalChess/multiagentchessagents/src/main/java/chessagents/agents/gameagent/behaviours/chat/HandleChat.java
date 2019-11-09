package chessagents.agents.gameagent.behaviours.chat;

import chessagents.agents.gameagent.GameAgent;
import chessagents.ontology.schemas.predicates.SaidTo;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import java.util.Optional;

public class HandleChat extends CyclicBehaviour {

    public static final String CHAT_PROTOCOL = "CHAT_PROTOCOL";
    private static final MessageTemplate MT = MessageTemplate.and(
            MessageTemplate.MatchProtocol(CHAT_PROTOCOL),
            MessageTemplate.MatchPerformative(ACLMessage.INFORM)
    );

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final InformSubscribersOfChat informSubscribersOfChat;
    private final HandleChatSubscriptions handleChatSubscriptions;

    public HandleChat(GameAgent gameAgent) {
        super(gameAgent);
        this.informSubscribersOfChat = new InformSubscribersOfChat();
        this.handleChatSubscriptions = new HandleChatSubscriptions(gameAgent, informSubscribersOfChat);
        gameAgent.addBehaviour(informSubscribersOfChat);
        gameAgent.addBehaviour(handleChatSubscriptions);
    }

    @Override
    public void action() {
        var message = myAgent.receive(MT);

        if (message != null) {
            handleChatMessage(message);
        } else {
            block();
        }
    }

    private void handleChatMessage(ACLMessage message) {
        extractChat(message).ifPresent(this::propagateChatToSubscribers);
    }

    private Optional<SaidTo> extractChat(ACLMessage message) {
        Optional<SaidTo> result = Optional.empty();

        try {
            var done = (Done) myAgent.getContentManager().extractContent(message);
            var action = (Action) done.getAction();
            var saidTo = (SaidTo) action.getAction();
            result = Optional.of(saidTo);
        } catch (Codec.CodecException | ClassCastException | OntologyException e) {
            logger.warning("Failed to extract contents: " + e.getMessage());
        }

        return result;
    }

    private void propagateChatToSubscribers(SaidTo saidTo) {
        informSubscribersOfChat.addEvent(saidTo);
    }

    @Override
    public void reset() {
        handleChatSubscriptions.reset();
        informSubscribersOfChat.reset();
        super.reset();
    }
}
