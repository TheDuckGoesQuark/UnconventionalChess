package chessagents.agents.pieceagent.argumentation;

import chessagents.ontology.schemas.concepts.PieceMove;

import java.util.LinkedHashMap;

public class TurnDiscussion {

    private final LinkedHashMap<PieceMove, MoveDiscussion> moveDiscussions = new LinkedHashMap<>();

    public void recordMessage(ConversationMessage conversationMessage) {
        var moveResponse = conversationMessage.getMoveResponse();

        // record messages not pointing to any move under null
        if (moveResponse.isEmpty()) {
            var discussion = moveDiscussions.computeIfAbsent(null, MoveDiscussion::new);
            discussion.addMessage(conversationMessage);
            return;
        }

        var optMove = moveResponse.get().getMove();

        if (optMove.isPresent()) {
            var move = optMove.get();
            var discussion = moveDiscussions.computeIfAbsent(move, MoveDiscussion::new);
            discussion.addMessage(conversationMessage);

            // moves this discussion to the top of the list by removing and reentering
            moveDiscussions.remove(move);
            moveDiscussions.put(move, discussion);
        }

        // check if an alternative was proposed that would alter the current move being discussed
        var alternative = moveResponse.get().getAlternativeResponse();
        if (alternative.isPresent()) {
            var alternativeMessage = new ConversationMessage(conversationMessage.getStatement(), alternative.get(), conversationMessage.getSender());
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

    public boolean proposalsCalledFor() {
        if (moveDiscussions.size() == 0) return false;

        // We can't just get the last element added so need to iterate :(
        var iter = moveDiscussions.entrySet().iterator();
        PieceMove lastElement = null;
        while (iter.hasNext()) lastElement = iter.next().getKey();
        return lastElement == null;
    }
}
