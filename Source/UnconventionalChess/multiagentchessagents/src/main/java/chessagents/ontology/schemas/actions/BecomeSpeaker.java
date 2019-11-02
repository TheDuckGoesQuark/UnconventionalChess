package chessagents.ontology.schemas.actions;

import jade.content.AgentAction;
import jade.content.OntoAID;
import jade.content.schema.AgentActionSchema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BecomeSpeaker implements AgentAction {

    private OntoAID agent;

}
