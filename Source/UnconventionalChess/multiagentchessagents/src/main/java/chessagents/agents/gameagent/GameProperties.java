package chessagents.agents.gameagent;

/**
 * Initialisation properties for game agent
 */
public class GameProperties {

    private final boolean humanPlays;
    private final boolean humanPlaysAsWhite;

    public GameProperties(boolean humanPlays, boolean humanPlaysAsWhite) {
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
