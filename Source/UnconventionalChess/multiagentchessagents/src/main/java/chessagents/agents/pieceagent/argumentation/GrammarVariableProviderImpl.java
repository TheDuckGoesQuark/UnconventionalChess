package chessagents.agents.pieceagent.argumentation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GrammarVariableProviderImpl implements GrammarVariableProvider {

    private final String moveTarget;
    private final String justification;
    private final String movingPiece;

    @Override
    public String getMoveTarget() {
        return moveTarget;
    }

    @Override
    public String getJustification() {
        return justification;
    }

    @Override
    public String getMovingPiece() {
        return movingPiece;
    }
}
