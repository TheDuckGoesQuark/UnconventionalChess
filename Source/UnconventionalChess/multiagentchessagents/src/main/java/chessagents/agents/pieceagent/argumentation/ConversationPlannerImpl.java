package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.commonbehaviours.RequestGameAgentMove;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.util.RandomUtil;

import java.util.HashSet;
import java.util.LinkedList;

public class ConversationPlannerImpl implements ConversationPlanner {

    /**
     * Agent that this planner is for
     */
    private final PieceAgent agent;
    /**
     * Discussions during each turn, with first move at first index
     */
    private final LinkedList<TurnDiscussion> turnDiscussions = new LinkedList<>();
    private final RandomUtil<ConversationMessage> RANDOM_MESSAGE_CHOOSER = new RandomUtil<>();

    public ConversationPlannerImpl(PieceAgent pieceAgent) {
        this.agent = pieceAgent;
        startNewTurn();
    }

    private TurnDiscussion getCurrentDiscussion() {
        return turnDiscussions.peekLast();
    }

    @Override
    public void handleConversationMessage(ConversationMessage conversationMessage) {
        getCurrentDiscussion().recordMessage(conversationMessage);
    }

    @Override
    public ConversationMessage produceMessage() {
        int numberOfMessages = getLengthOfCurrentDiscussion();

        final ConversationMessage conversationMessage;
        if (numberOfMessages == 0) {
            conversationMessage = startDiscussion();
        } else {
            conversationMessage = continueDiscussion();
        }

        // if choice of next message involves agreeing and performing the move,
        // then send the move to the game agent to be processed
        if (conversationMessage.movePerformed()) {
            var makeMove = new MakeMove(conversationMessage.getMoveResponse().getMove().get());
            var gameAgentAID = agent.getPieceContext().getGameAgentAID();
            agent.addBehaviour(new RequestGameAgentMove(makeMove, gameAgentAID));
        }

        // record our message
        handleConversationMessage(conversationMessage);

        return conversationMessage;
    }

    private ConversationMessage startDiscussion() {
        var options = new HashSet<ConversationMessage>();
        // propose move
        // perform move
        // ask for proposals
        return RANDOM_MESSAGE_CHOOSER.chooseRandom(options);
    }

    private ConversationMessage continueDiscussion() {
        // no proposals so far -> propose or perform move
        // else -> get last proposal and determine reaction
        return null;
    }

    @Override
    public void startNewTurn() {
        turnDiscussions.add(new TurnDiscussion());
    }

    @Override
    public int getLengthOfCurrentDiscussion() {
        return getCurrentDiscussion().getNumberOfMessages();
    }
}
