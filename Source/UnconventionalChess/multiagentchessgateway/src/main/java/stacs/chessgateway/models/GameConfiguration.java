package stacs.chessgateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class GameConfiguration {

    private boolean humanPlays;
    private boolean humanPlaysAsWhite;
    private int gameId;
    private Map<String, PieceConfig> pieceConfigs;

}
