package stacs.chessgateway.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PieceConfig {

    private String name;
    private PersonalityType personality;

}
