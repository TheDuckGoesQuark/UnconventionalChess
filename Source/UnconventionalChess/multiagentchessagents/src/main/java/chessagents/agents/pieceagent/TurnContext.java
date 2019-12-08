package chessagents.agents.pieceagent;

import chessagents.ontology.schemas.concepts.PieceMove;
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
    private PieceMove currentMove = null;
    private ACLMessage currentMessage = null;
    private int debateCycles = 0;

    public void reset() {
        currentSpeaker = null;
        currentMove = null;
        currentMessage = null;
        debateCycles = 0;
    }
}
