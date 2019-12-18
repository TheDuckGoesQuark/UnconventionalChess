package chessagents.agents.pieceagent.argumentation;

import jade.core.AID;

import java.io.Serializable;
import java.util.Optional;

public class ConversationMessage implements Serializable {

    private final MoveResponse moveResponse;
    private final AID sender;
    private final String quip;

    public ConversationMessage(String quip, AID sender) {
        this.moveResponse = null;
        this.quip = quip;
        this.sender = sender;
    }

    public ConversationMessage(MoveResponse moveResponse, AID sender) {
        this.moveResponse = moveResponse;
        this.quip = null;
        this.sender = sender;
    }

    boolean movePerformed() {
        return getMoveResponse()
                .map(MoveResponse::isPerformed)
                .orElse(false);
    }

    public Optional<String> getQuip() {
        return Optional.ofNullable(quip);
    }

    public Optional<MoveResponse> getMoveResponse() {
        return Optional.ofNullable(moveResponse);
    }

    public AID getSender() {
        return sender;
    }

    public String getAsHumanFriendlyString() {
        return getQuip()
                .orElse(getMoveResponse()
                        .map(moveResponse -> moveResponse.getOpinionGeneratingValue())
                        .orElse(""));
    }
}
