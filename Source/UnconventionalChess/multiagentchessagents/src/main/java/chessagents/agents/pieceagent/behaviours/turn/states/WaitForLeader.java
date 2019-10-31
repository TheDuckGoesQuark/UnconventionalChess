package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.predicates.IsLeader;
import jade.content.OntoAID;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsVariable;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import jade.util.Logger;

/**
 * Requests to know who the leader is at the start of the turn
 */
public class WaitForLeader extends SimpleAchieveREInitiator {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final TurnContext turnContext;
    private final PieceContext pieceContext;

    public WaitForLeader(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext, DataStore dataStore) {
        super(pieceAgent, new ACLMessage(ACLMessage.QUERY_REF), dataStore);
        this.turnContext = turnContext;
        this.pieceContext = pieceContext;
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage request) {
        request.addReceiver(pieceContext.getGameAgentAID());
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);
        request.setOntology(ChessOntology.ONTOLOGY_NAME);
        request.setEncoding(FIPANames.ContentLanguage.FIPA_SL);

        var absAID = new AbsVariable("leader", ChessOntology.IS_LEADER_AGENT);
        var absIsLeader = new AbsPredicate(ChessOntology.IS_LEADER);
        absIsLeader.set(ChessOntology.IS_LEADER_AGENT, absAID);

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
}
