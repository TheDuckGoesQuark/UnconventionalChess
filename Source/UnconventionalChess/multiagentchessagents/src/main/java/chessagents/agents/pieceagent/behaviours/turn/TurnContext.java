package chessagents.agents.pieceagent.behaviours.turn;

import chessagents.ontology.schemas.concepts.Move;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TurnContext {
    private AID currentSpeaker = null;
    private Move currentMove = null;
    private ACLMessage currentMessage = null;

    public void reset() {
        currentSpeaker = null;
        currentMove = null;
        currentMessage = null;
    }
}
