package stacs.chessgateway.models;

import chessagents.agents.gatewayagent.messages.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Message<T> {

    private Instant timestamp;
    private MessageType type;
    private T body;

    public Message(Instant timestamp, MessageType type, @JsonProperty("body") T body) {
        this.timestamp = timestamp;
        this.type = type;
        this.body = body;
    }

}
