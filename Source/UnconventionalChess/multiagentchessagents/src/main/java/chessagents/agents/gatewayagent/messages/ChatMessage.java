package chessagents.agents.gatewayagent.messages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    private String fromId;
    private String messageBody;

}
