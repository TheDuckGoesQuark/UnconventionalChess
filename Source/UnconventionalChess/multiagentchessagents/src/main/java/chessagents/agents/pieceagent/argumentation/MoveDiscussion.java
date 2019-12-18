package chessagents.agents.pieceagent.argumentation;

import chessagents.ontology.schemas.concepts.PieceMove;

import java.util.LinkedList;
import java.util.Objects;

/**
 * A record of all the messages that were in regards to the given move
 */
public class MoveDiscussion {

    private final LinkedList<ConversationMessage> discussion = new LinkedList<>();
    private final PieceMove pieceMove;

    public MoveDiscussion(PieceMove pieceMove) {
        this.pieceMove = pieceMove;
    }

    public int getNumberOfMessages() {
        return discussion.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveDiscussion that = (MoveDiscussion) o;
        return pieceMove.equals(that.pieceMove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceMove);
    }
}
