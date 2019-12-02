package chessagents.ontology.schemas.actions;

import chessagents.ontology.schemas.concepts.Game;
import jade.content.AgentAction;
import jade.util.leap.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateGame implements AgentAction {

    private Game game;
    private List pieceConfigurations;

}
