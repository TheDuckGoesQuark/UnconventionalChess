package chessagents.agents.pieceagent.argumentation;

import chessagents.ontology.schemas.concepts.PieceMove;
import jade.core.AID;

import java.util.Optional;

public class ConversationMessage {

    private final MoveResponse moveResponse;
    private final AID sender;

    public ConversationMessage(MoveResponse moveResponse, AID sender) {
        this.moveResponse = moveResponse;
        this.sender = sender;
    }

    String getAsHumanFriendlyString() {
        return null;
    }

    boolean movePerformed() {
        return moveResponse.performed();
    }

    Optional<PieceMove> getMoveDiscussed() {
        return Optional.ofNullable(moveResponse.getMove());
    }

}
