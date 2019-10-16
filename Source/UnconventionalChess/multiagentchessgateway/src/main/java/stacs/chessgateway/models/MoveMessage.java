package stacs.chessgateway.models;

public class MoveMessage {

    private final String sourceSquare;
    private final String targetSquare;

    public MoveMessage(String sourceSquare, String targetSquare) {
        this.sourceSquare = sourceSquare;
        this.targetSquare = targetSquare;
    }

    public String getSourceSquare() {
        return sourceSquare;
    }

    public String getTargetSquare() {
        return targetSquare;
    }

    @Override
    public String toString() {
        return "MoveMessage{" +
                ", sourceSquare='" + sourceSquare + '\'' +
                ", targetSquare='" + targetSquare + '\'' +
                '}';
    }
}
