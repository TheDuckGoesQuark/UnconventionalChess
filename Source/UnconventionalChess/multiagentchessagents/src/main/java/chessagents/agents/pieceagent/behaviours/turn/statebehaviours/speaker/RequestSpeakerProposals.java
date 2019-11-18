package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.lang.acl.ACLMessage;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.PROPOSALS_REQUESTED;

public class RequestSpeakerProposals extends PieceStateBehaviour {

    public static final String SPEAKER_CONTRACT_NET_PROTOCOL = "SPEAKER_CONTRACT_NET_PROTOCOL";
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public RequestSpeakerProposals(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.REQUEST_SPEAKER_PROPOSALS);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        requestProposals();
        setEvent(PROPOSALS_REQUESTED);
    }

    private void requestProposals() {
        var cfp = ChessMessageBuilder.constructMessage(ACLMessage.CFP);

        // send to everyone on my side (including myself!)
        pieceContext.getGameState().getAllPieces().stream()
                .filter(p -> p.getAgentAID() != null)
                .filter(p -> p.getColour().equals(pieceContext.getMyColour()))
                .map(ChessPiece::getAgentAID)
                .forEach(cfp::addReceiver);

        cfp.setProtocol(SPEAKER_CONTRACT_NET_PROTOCOL);

        // Store CFP
        turnContext.setCurrentMessage(cfp);

        myAgent.send(cfp);
    }
}
