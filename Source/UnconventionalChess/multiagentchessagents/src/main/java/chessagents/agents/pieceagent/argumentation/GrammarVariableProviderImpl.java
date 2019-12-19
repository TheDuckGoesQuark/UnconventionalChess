package chessagents.agents.pieceagent.argumentation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GrammarVariableProviderImpl implements GrammarVariableProvider {

    private final MoveResponse moveResponse;
    private final String movingPiece;
    private final String alternativeMovingPiece;

    @Override
    public String getMoveTarget() {
        return moveResponse.getMove().get().getTarget().getCoordinates();
    }

    @Override
    public String getJustification() {
        return moveResponse.getReasoning().getJustification();
    }

    @Override
    public String getMovingPiece() {
        return movingPiece;
    }

    @Override
    public String getAlternativeMoveTarget() {
        return moveResponse.getAlternativeResponse().get().getMove().get().getTarget().getCoordinates();
    }

    @Override
    public String getAlternativeMovingPiece() {
        return alternativeMovingPiece;
    }

    @Override
    public String getAlternativeJustification() {
        return moveResponse.getAlternativeResponse().get().getReasoning().getJustification();

    }
}
