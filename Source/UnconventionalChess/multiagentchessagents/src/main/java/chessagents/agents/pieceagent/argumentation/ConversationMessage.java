package chessagents.agents.pieceagent.argumentation;

import jade.core.AID;

public class ConversationMessage {

    private final MoveResponse moveResponse;
    private final AID sender;

    public ConversationMessage(MoveResponse moveResponse, AID sender) {
        this.moveResponse = moveResponse;
        this.sender = sender;
    }

    boolean movePerformed() {
        return moveResponse.performed();
    }

    public MoveResponse getMoveResponse() {
        return moveResponse;
    }

    public AID getSender() {
        return sender;
    }
}
