package chessagents.agents.pieceagent.argumentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GrammarVariableProviderImpl implements GrammarVariableProvider {

    private MoveResponse moveResponse;
    private String movingPiece;
    private String alternativeMovingPiece;
    private String capturedPiece;
    private String escapingPiece;

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
