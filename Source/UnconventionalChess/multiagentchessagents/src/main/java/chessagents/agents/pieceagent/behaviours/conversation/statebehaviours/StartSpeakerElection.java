package chessagents.agents.pieceagent.behaviours.conversation.statebehaviours;

import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationContext;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationState;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.lang.acl.ACLMessage;

public class StartSpeakerElection extends ConversationStateBehaviour {

    public static final String SPEAKER_ELECTION_PROTOCOL_NAME = "SPEAKER_ELECTION_PROTOCOL";

    public StartSpeakerElection(PieceAgent a, ConversationContext conversationContext) {
        super(a, ConversationState.START_SPEAKER_ELECTION, conversationContext);
    }

    @Override
    public void action() {
        requestProposals();
        setTransition(ConversationTransition.STARTED_SPEAKER_ELECTION);
    }

    private void requestProposals() {
        var cfp = ChessMessageBuilder.constructMessage(ACLMessage.CFP);

        // send to everyone on my side (including myself!)
        var myAgent = getAgent();
        var context = myAgent.getPieceContext();
        var gameState = context.getGameState();
        gameState.getAllAgentPiecesForColourOnBoard(context.getMyPiece().getColour())
                .stream()
                .map(ChessPiece::getAgentAID)
                .forEach(cfp::addReceiver);

        cfp.setProtocol(SPEAKER_ELECTION_PROTOCOL_NAME);
        cfp.setConversationId(getConversationContext().getConversationID());
        myAgent.send(cfp);
    }
}
