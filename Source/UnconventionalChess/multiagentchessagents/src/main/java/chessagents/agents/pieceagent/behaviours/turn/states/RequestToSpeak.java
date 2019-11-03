package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.fsm.PieceTransition.REQUESTED_TO_SPEAK;

public class RequestToSpeak extends OneShotBehaviour implements PieceStateBehaviour {
    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public RequestToSpeak(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public int getNextTransition() {
        return REQUESTED_TO_SPEAK.ordinal();
    }

    @Override
    public void action() {
        logger.info("Requesting to speak");
        var cfp = turnContext.getCurrentMessage();
        var proposal = constructProposal(cfp);
        myAgent.send(proposal);
    }

    private ACLMessage constructProposal(ACLMessage cfp) {
        var myAID = myAgent.getAID();
        var ontoAID = new OntoAID(myAID.getName(), AID.ISGUID);
        var becomeSpeaker = new BecomeSpeaker(ontoAID);
        var action = new Action(myAID, becomeSpeaker);
        var proposal = cfp.createReply();
        proposal.setPerformative(ACLMessage.PROPOSE);

        try {
            myAgent.getContentManager().fillContent(proposal, action);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to build proposal to become speaker: " + e.getMessage());
        }

        return proposal;
    }
}
