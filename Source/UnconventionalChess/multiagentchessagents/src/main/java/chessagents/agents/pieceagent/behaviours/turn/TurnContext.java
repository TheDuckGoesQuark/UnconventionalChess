package chessagents.agents.pieceagent.behaviours.turn;

import jade.core.AID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TurnContext {
    private AID currentSpeaker;

    public void reset() {
        currentSpeaker = null;
    }
}
