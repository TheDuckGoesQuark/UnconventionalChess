package chessagents.agents.pieceagent.behaviours.conversation.statebehaviours;

import chessagents.agents.commonbehaviours.RequestGameAgentMove;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationContext;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationState;
import chessagents.agents.pieceagent.behaviours.conversation.ConversationTransition;

public class MakeMove extends ConversationStateBehaviour {

    private RequestGameAgentMove requestGameAgentMove = null;

    public MakeMove(PieceAgent a, ConversationContext conversationContext) {
        super(a, ConversationState.MAKE_MOVE, conversationContext);
    }

    @Override
    public void reset() {
        requestGameAgentMove = null;
        super.reset();
    }

    @Override
    public void action() {
        if (requestGameAgentMove == null) {
            addBehaviour();
        } else if (requestGameAgentMove.wasSuccessful()) {
            setTransition(ConversationTransition.MOVE_MADE);
        } else {
            block();
        }
    }

    private void addBehaviour() {
        var lastMove = getConversationContext().getLastMoveDiscussed();
        var makeMove = new chessagents.ontology.schemas.actions.MakeMove(lastMove);
        var gameAgentAID = getAgent().getPieceContext().getGameAgentAID();
        requestGameAgentMove = new RequestGameAgentMove(makeMove, gameAgentAID);
        requestGameAgentMove.addCallbackBehaviour(this);
        getAgent().addBehaviour(requestGameAgentMove);
    }
}
