package stacs.chessgateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PieceConfig {

    private String name;
    private PersonalityType personality;

}
