package stacs.chessgateway.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class GameConfiguration {

    private boolean humanPlays;
    private boolean humanPlaysAsWhite;
    private Map<String, PieceConfig> pieceConfigs;

    @JsonInclude(Include.NON_NULL) // ignore gameId if missing
    private Integer gameId;

}
