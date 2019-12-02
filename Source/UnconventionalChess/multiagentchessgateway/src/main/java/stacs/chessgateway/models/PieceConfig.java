package stacs.chessgateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PieceConfig {

    private String name;
    private PersonalityType personality;

}
