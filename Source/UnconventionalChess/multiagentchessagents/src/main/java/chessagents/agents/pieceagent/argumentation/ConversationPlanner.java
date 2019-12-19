package chessagents.agents.pieceagent.argumentation;

import chessagents.ontology.schemas.concepts.PieceMove;

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
     * the move will have been sent to the game agent and performed.
     *
     * @return this agents next input into the discussion
     */
    ConversationMessage produceMessage();

    /**
     * Starts a new turn discussion
     */
    void startNewTurn();

    /**
     * Get the number of messages exchanged during the current discussion
     *
     * @return the number of messages exchanged during th current discussion
     */
    int getLengthOfCurrentDiscussion();

    PieceMove getLastMoveDiscussed();
}
