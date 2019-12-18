package chessagents.agents.pieceagent.argumentation;

public interface ConversationPlanner {

    /**
     * Records the given conversation message and updates internal state accordingly.
     *
     * @param conversationMessage message that has been received
     */
    void handleConversationMessage(ConversationMessage conversationMessage);

    /**
     * Produces a message given the argument so far.
     * <p>
     * If {@link ConversationMessage#movePerformed()} is true,
     * then the move in the {@link ConversationMessage#getMoveDiscussed()} will
     * have been sent to the game agent and performed.
     *
     * @return this agents next input into the discussion
     */
    ConversationMessage produceMessage();

    /**
     * Starts a new turn discussion
     */
    void startNewTurn();

}
