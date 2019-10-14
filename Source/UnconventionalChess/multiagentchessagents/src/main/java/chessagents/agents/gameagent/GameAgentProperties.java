package chessagents.agents.gameagent;

/**
 * Initialisation properties for game agent
 */
public class GameAgentProperties {

    private final boolean humanPlays;
    private final boolean humanPlaysAsWhite;

    public GameAgentProperties(boolean humanPlays, boolean humanPlaysAsWhite) {
        this.humanPlays = humanPlays;
        this.humanPlaysAsWhite = humanPlaysAsWhite;
    }

    public boolean isHumanPlays() {
        return humanPlays;
    }

    public boolean isHumanPlaysAsWhite() {
        return humanPlaysAsWhite;
    }
}
