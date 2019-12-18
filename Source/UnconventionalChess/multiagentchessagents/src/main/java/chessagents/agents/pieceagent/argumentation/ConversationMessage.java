package chessagents.agents.pieceagent.argumentation;

import chessagents.ontology.schemas.concepts.PieceMove;

import java.util.Optional;

public interface ConversationMessage {

    String getAsHumanFriendlyString();

    boolean movePerformed();

    Optional<PieceMove> getMoveDiscussed();

}
