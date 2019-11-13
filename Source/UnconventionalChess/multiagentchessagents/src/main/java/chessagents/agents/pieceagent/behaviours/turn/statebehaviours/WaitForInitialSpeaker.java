package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.ChessOntology;
import jade.content.OntoAID;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsVariable;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import static chessagents.agents.gameagent.behaviours.gameplay.ElectLeaderAgent.ELECT_SPEAKER_PROTOCOL_NAME;
import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.I_AM_SPEAKER;
import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.I_AM_NOT_SPEAKER;

/**
 * Requests to know who the speaker is at the start of the turn
 */
public class WaitForInitialSpeaker extends PieceStateBehaviour {

    enum RequestState {
        REQUESTING, WAIT_ON_AGREE, WAIT_ON_INFORM, INFORMED
    }

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final TurnContext turnContext;
    private final PieceContext pieceContext;
    private RequestState requestState = RequestState.REQUESTING;
    private ACLMessage request;
    private MessageTemplate conversationIDMatcher = null;

    public WaitForInitialSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.WAIT_FOR_INITIAL_SPEAKER);
        this.turnContext = turnContext;
        this.pieceContext = pieceContext;
    }

    @Override
    protected void initialiseState() {
        requestState = RequestState.REQUESTING;
        request = prepareRequest(ChessMessageBuilder.constructMessage(ACLMessage.QUERY_REF));
        conversationIDMatcher = MessageTemplate.MatchConversationId(request.getConversationId());
    }

    @Override
    public void action() {
        switch (requestState) {
            case REQUESTING:
                // send request
                myAgent.send(request);
                requestState = RequestState.WAIT_ON_AGREE;
                break;
            case WAIT_ON_AGREE:
                var agree = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.AGREE), conversationIDMatcher));

                if (agree != null) requestState = RequestState.WAIT_ON_INFORM;
                else block();
                break;
            case WAIT_ON_INFORM:
                var inform = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), conversationIDMatcher));

                if (inform != null) {
                    handleInform(inform);
                    requestState = RequestState.INFORMED;
                } else block();
                break;
            case INFORMED:
                break;
        }
    }

    private ACLMessage prepareRequest(ACLMessage request) {
        request.addReceiver(pieceContext.getGameAgentAID());
        request.setProtocol(ELECT_SPEAKER_PROTOCOL_NAME);

        var absAID = new AbsVariable("leader", ChessOntology.IS_SPEAKER_AGENT);
        var absIsSpeaker = new AbsPredicate(ChessOntology.IS_SPEAKER);
        absIsSpeaker.set(ChessOntology.IS_SPEAKER_AGENT, absAID);

        var ire = new AbsIRE(SLVocabulary.IOTA);
        ire.setVariable(absAID);
        ire.setProposition(absIsSpeaker);

        try {
            myAgent.getContentManager().fillContent(request, ire);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to construct request for leader: " + e.getMessage());
        }

        return request;
    }

    private void handleInform(ACLMessage inform) {
        try {
            var abs = (AbsPredicate) myAgent.getContentManager().extractAbsContent(inform);

            if (abs.getTypeName().equals(BasicOntology.EQUALS)) {
                var right = abs.getAbsTerm(BasicOntology.EQUALS_RIGHT);
                var leaderAID = (OntoAID) ChessOntology.getInstance().toObject(right);
                turnContext.setCurrentSpeaker(leaderAID);
                var imSpeaker = turnContext.getCurrentSpeaker().equals(myAgent.getAID());
                setEvent(imSpeaker ? I_AM_SPEAKER : I_AM_NOT_SPEAKER);
            } else {
                throw new NotUnderstoodException("Did not receive answer to query?");
            }
        } catch (Codec.CodecException | OntologyException | NotUnderstoodException e) {
            logger.warning("Failed when receiving inform to speaker query: " + e.getMessage());
        }
    }
}
