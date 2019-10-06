package stacs.chessgateway.models;

public class GameConfiguration {

    private boolean humanPlays;
    private boolean humanPlaysAsWhite;
    private int gameId;

    public GameConfiguration() {
    }

    public boolean isHumanPlays() {
        return humanPlays;
    }

    public void setHumanPlays(boolean humanPlays) {
        this.humanPlays = humanPlays;
    }

    public boolean isHumanPlaysAsWhite() {
        return humanPlaysAsWhite;
    }

    public void setHumanPlaysAsWhite(boolean humanPlaysAsWhite) {
        this.humanPlaysAsWhite = humanPlaysAsWhite;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
