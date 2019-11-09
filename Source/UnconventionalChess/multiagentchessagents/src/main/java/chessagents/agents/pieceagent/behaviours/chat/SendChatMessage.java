package chessagents.agents.pieceagent.behaviours.chat;

import chessagents.agents.ChessMessageBuilder;
import chessagents.ontology.schemas.predicates.SaidTo;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class SendChatMessage extends OneShotBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final String contents;
    private final AID speaker;
    private final AID messageBroker;

    public SendChatMessage(String contents, AID speaker, AID messageBroker) {
        this.contents = contents;
        this.speaker = speaker;
        this.messageBroker = messageBroker;
    }

    @Override
    public void action() {
        var saidTo = new SaidTo(new OntoAID(speaker.getName(), AID.ISGUID), contents);
        var message = ChessMessageBuilder.constructMessage(ACLMessage.INFORM);
        message.addReceiver(messageBroker);

        try {
            myAgent.getContentManager().fillContent(message, saidTo);
            myAgent.send(message);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to send chat message: " + e.getMessage());
        }
    }
}
