package chessagents.agents.pieceagent.argumentation;

import chessagents.ontology.schemas.concepts.PieceMove;

import java.util.LinkedHashMap;

public class TurnDiscussion {

    private final LinkedHashMap<PieceMove, MoveDiscussion> moveDiscussions = new LinkedHashMap<>();

    public void recordMessage(ConversationMessage conversationMessage) {
        var moveResponse = conversationMessage.getMoveResponse();
        var optMove = moveResponse.getMove();

        if (optMove.isPresent()) {
            var move = optMove.get();
            var discussion = moveDiscussions.computeIfAbsent(move, MoveDiscussion::new);
            discussion.addMessage(conversationMessage);

            // moves this discussion to the top of the list by removing and reentering
            moveDiscussions.remove(move);
            moveDiscussions.put(move, discussion);
        }

        // check if an alternative was proposed that would alter the current move being discussed
        var alternative = moveResponse.getAlternativeResponse();
        if (alternative.isPresent()) {
            var alternativeMessage = new ConversationMessage(alternative.get(), conversationMessage.getSender());
            recordMessage(alternativeMessage);
        }
    }

    public int getNumberOfMessages() {
        return moveDiscussions
                .values()
                .stream()
                .map(MoveDiscussion::getNumberOfMessages)
                .reduce(0, Integer::sum);
    }
}
