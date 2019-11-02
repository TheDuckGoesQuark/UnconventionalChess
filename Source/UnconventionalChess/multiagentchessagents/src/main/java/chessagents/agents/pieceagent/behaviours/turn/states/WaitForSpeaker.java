package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
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
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import jade.util.Logger;

import static chessagents.agents.gameagent.behaviours.gameplay.ElectLeaderAgent.ELECT_LEADER_PROTOCOL_NAME;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.I_AM_SPEAKER;
import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.I_AM_NOT_SPEAKER;

/**
 * Requests to know who the speaker is at the start of the turn
 */
public class WaitForSpeaker extends SimpleAchieveREInitiator {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final TurnContext turnContext;
    private final PieceContext pieceContext;

    public WaitForSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, new ACLMessage(ACLMessage.QUERY_REF));
        this.turnContext = turnContext;
        this.pieceContext = pieceContext;
    }

    @Override
    public void onStart() {
        logger.info("Waiting for leader");
        super.onStart();
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage request) {
        // JADE Impl resets the internal data store which nulls our request we gave it in the constructor.
        // I lost at least two hours to that nonsense. So now we need this check.
        if (request == null) request = new ACLMessage(ACLMessage.QUERY_REF);

        request.addReceiver(pieceContext.getGameAgentAID());
        request.setProtocol(ELECT_LEADER_PROTOCOL_NAME);
        request.setOntology(ChessOntology.ONTOLOGY_NAME);
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);

        var absAID = new AbsVariable("leader", ChessOntology.IS_SPEAKER_AGENT);
        var absIsLeader = new AbsPredicate(ChessOntology.IS_SPEAKER);
        absIsLeader.set(ChessOntology.IS_SPEAKER_AGENT, absAID);

        var ire = new AbsIRE(SLVocabulary.IOTA);
        ire.setVariable(absAID);
        ire.setProposition(absIsLeader);

        try {
            myAgent.getContentManager().fillContent(request, ire);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to construct request for leader: " + e.getMessage());
        }

        return super.prepareRequest(request);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            var abs = (AbsPredicate) myAgent.getContentManager().extractAbsContent(inform);

            if (abs.getTypeName().equals(BasicOntology.EQUALS)) {
                var right = abs.getAbsTerm(BasicOntology.EQUALS_RIGHT);
                var leaderAID = (OntoAID) ChessOntology.getInstance().toObject(right);
                turnContext.setCurrentSpeaker(leaderAID);
                logger.info("Speaker realised as " + turnContext.getCurrentSpeaker().getName());
            } else {
                throw new NotUnderstoodException("Did not receive answer to query?");
            }

        } catch (Codec.CodecException | OntologyException | NotUnderstoodException e) {
            logger.warning("Failed when receiving inform to leader query: " + e.getMessage());
        }
    }

    @Override
    public boolean done() {
        return turnContext.getCurrentSpeaker() != null;
    }

    @Override
    public int onEnd() {
        return (turnContext.getCurrentSpeaker().equals(myAgent.getAID()) ? I_AM_SPEAKER : I_AM_NOT_SPEAKER).ordinal();
    }
}
