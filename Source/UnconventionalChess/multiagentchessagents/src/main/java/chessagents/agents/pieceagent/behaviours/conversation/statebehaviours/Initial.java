package chessagents.agents.pieceagent.behaviours.conversation.statebehaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationContext;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationState;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationTransition;
import com.github.bhlangonijr.chesslib.PieceType;

public class Initial extends ConversationStateBehaviour {
    public Initial(PieceAgent pieceAgent, ConversationContext conversationContext) {
        super(pieceAgent, ConversationState.INITIAL, conversationContext);
    }

    @Override
    public void action() {
        var agent = getAgent();
        var pieceContext = agent.getPieceContext();
        var conversationContext = getConversationContext();

        // if no speaker or current speaker has died, assume king
        if (conversationContext.getSpeaker() == null || !speakerAlive()) {
            var myPiece = pieceContext.getMyPiece();

            var kingTypeName = PieceType.KING.value();
            if (myPiece.getType().equals(kingTypeName)) {
                conversationContext.setSpeaker(myPiece.getAgentAID());
                setTransition(ConversationTransition.IS_SPEAKER);
            } else {
                setTransition(ConversationTransition.NOT_SPEAKER);
                var kingAID = pieceContext.getGameState().getAllPiecesForTypeAndColour(kingTypeName, myPiece.getColour()).stream().findFirst().get().getAgentAID();
                conversationContext.setSpeaker(kingAID);
            }
        }

        if (conversationContext.getSpeaker().equals(agent.getAID())) {
            setTransition(ConversationTransition.IS_SPEAKER);
        } else {
            setTransition(ConversationTransition.NOT_SPEAKER);
        }
    }

    private boolean speakerAlive() {
        var pieceContext = getAgent().getPieceContext();
        // agents are single threaded so no race conditions on the game state update when a move arrives
        return pieceContext.getGameState().getAgentPieceWithAID(getConversationContext().getSpeaker()).isPresent();
    }
}
