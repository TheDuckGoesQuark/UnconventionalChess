package chessagents.agents.gameagent;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Initialisation properties for game agent
 */
@AllArgsConstructor
@Getter
public class GameProperties {

    private final boolean humanPlays;
    private final boolean humanPlaysAsWhite;

    public boolean isHumanPlays() {
        return humanPlays;
    }

    public boolean isHumanPlaysAsWhite() {
        return humanPlaysAsWhite;
    }
}
