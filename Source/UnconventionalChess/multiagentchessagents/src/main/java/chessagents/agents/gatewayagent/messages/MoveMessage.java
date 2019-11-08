package chessagents.agents.gatewayagent.messages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 */
@NoArgsConstructor
@Getter
@Setter
public class MoveMessage {

    private String sourceSquare;
    private String targetSquare;
    private String promotion;

}
