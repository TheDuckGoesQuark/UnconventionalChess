package chessagents.agents.pieceagent.argumentation;

import jade.core.AID;

import java.io.Serializable;
import java.util.Optional;

public class ConversationMessage implements Serializable {

    private final MoveResponse moveResponse;
    private final AID sender;
    private final String statement;

    public ConversationMessage(String statement, AID sender) {
        this.statement = statement;
        this.sender = sender;
        this.moveResponse = null;
    }

    public ConversationMessage(String statement, MoveResponse moveResponse, AID sender) {
        this.statement = statement;
        this.sender = sender;
        this.moveResponse = moveResponse;
    }

    public boolean movePerformed() {
        return getMoveResponse()
                .map(MoveResponse::isPerformed)
                .orElse(false);
    }

    public String getStatement() {
        return statement;
    }

    public Optional<MoveResponse> getMoveResponse() {
        return Optional.ofNullable(moveResponse);
    }

    public AID getSender() {
        return sender;
    }
}
