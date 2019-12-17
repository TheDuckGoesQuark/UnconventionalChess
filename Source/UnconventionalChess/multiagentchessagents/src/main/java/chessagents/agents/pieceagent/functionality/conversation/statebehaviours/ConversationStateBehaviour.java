package chessagents.agents.pieceagent.functionality.conversation.statebehaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.functionality.conversation.ConversationContext;
import chessagents.agents.pieceagent.functionality.conversation.ConversationState;
import chessagents.agents.pieceagent.functionality.conversation.ConversationTransition;
import jade.core.behaviours.Behaviour;
import jade.util.Logger;
import lombok.Getter;

@Getter
public abstract class ConversationStateBehaviour extends Behaviour {

    protected final Logger logger = Logger.getMyLogger(getClass().getName());
    private final ConversationState state;
    private final ConversationContext conversationContext;
    private ConversationTransition transition = null;

    public ConversationStateBehaviour(PieceAgent a, ConversationState state, ConversationContext conversationContext) {
        super(a);
        this.state = state;
        this.conversationContext = conversationContext;
    }

    @Override
    public PieceAgent getAgent() {
        return (PieceAgent) myAgent;
    }

    @Override
    public void onStart() {
        logger.info(String.format("STATE: %s", state.name()));
    }

    protected void setTransition(ConversationTransition transition) {
        this.transition = transition;
    }

    @Override
    public int onEnd() {
        return transition.ordinal();
    }

    @Override
    public boolean done() {
        return transition != null;
    }

    @Override
    public void reset() {
        transition = null;
        super.reset();
    }
}
