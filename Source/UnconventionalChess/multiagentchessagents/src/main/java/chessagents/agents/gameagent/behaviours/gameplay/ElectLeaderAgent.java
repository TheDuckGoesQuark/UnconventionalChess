package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import chessagents.ontology.ChessOntology;
import jade.content.OntoAID;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ElectLeaderAgent extends SimpleBehaviour {

    private static final Random random = new Random();
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final GameContext context;
    private final Set<ACLMessage> requests = new HashSet<>();
    private boolean leaderChosen = false;

    ElectLeaderAgent(GameAgent myAgent, GameContext context) {
        super(myAgent);
        this.context = context;
    }

    @Override
    public void action() {
        var message = myAgent.receive();

        if (message != null && message.getPerformative() == ACLMessage.QUERY_REF) {
            if (isLeaderQuery(message)) {
                handleQuery(message);
            } else {
                replyNotUnderstood(message);
            }
        }

        block();
    }

    private void replyNotUnderstood(ACLMessage message) {
        var notUnderstood = message.createReply();
        notUnderstood.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        myAgent.send(notUnderstood);
    }

    private boolean isLeaderQuery(ACLMessage message) {
        var isLeaderQuery = false;
        try {
            var ire = (AbsIRE) myAgent.getContentManager().extractAbsContent(message);
            var prop = ire.getProposition();
            isLeaderQuery = prop.getTypeName().equals(ChessOntology.IS_LEADER);
        } catch (Codec.CodecException | OntologyException | ClassCastException e) {
            logger.warning("Failed to extract leader query from message");
        }
        return isLeaderQuery;
    }

    private boolean receivedRequestFromEveryone() {
        return requests.size() == context.getPiecesByAID().size();
    }

    private void handleQuery(ACLMessage message) {
        sendAgree(message);
        requests.add(message);

        if (receivedRequestFromEveryone()) {
            var chosenOne = chooseLeader();
            informEveryoneOfTheChosenOne(chosenOne);
        }
    }

    private void informEveryoneOfTheChosenOne(AID chosenOne) {
        try {
            var ire = (AbsIRE) myAgent.getContentManager().extractAbsContent(requests.iterator().next());
            var ontoAid = new OntoAID(chosenOne.getName(), AID.ISGUID);
            var equals = new AbsPredicate(BasicOntology.EQUALS);
            equals.set(BasicOntology.EQUALS_LEFT, ire);
            equals.set(BasicOntology.EQUALS_RIGHT, ChessOntology.getInstance().fromObject(ontoAid));

            for (ACLMessage r : requests) {
                informOfTheChosenOne(r, equals);
            }
        } catch (Codec.CodecException | ClassCastException | OntologyException e) {
            logger.warning("Failed to inform everyone of the chosen one: " + e.getMessage());
        } finally {
            leaderChosen = true;
        }
    }

    private void informOfTheChosenOne(ACLMessage request, AbsPredicate answer) throws Codec.CodecException, OntologyException {
        var inform = request.createReply();
        inform.setPerformative(ACLMessage.INFORM_REF);
        myAgent.getContentManager().fillContent(inform, answer);
        myAgent.send(inform);
    }

    private AID chooseLeader() {
        var requestMessages = (ACLMessage[]) requests.toArray();
        return requestMessages[random.nextInt(requestMessages.length)].getSender();
    }

    private void sendAgree(ACLMessage message) {
        var agree = message.createReply();
        agree.setPerformative(ACLMessage.AGREE);
        myAgent.send(agree);
    }

    @Override
    public boolean done() {
        return leaderChosen;
    }

    @Override
    public void reset() {
        requests.clear();
        leaderChosen = false;
        super.reset();
    }

    @Override
    public int onEnd() {
        return GamePlayTransition.LEADER_AGENT_CHOSEN.ordinal();
    }
}
