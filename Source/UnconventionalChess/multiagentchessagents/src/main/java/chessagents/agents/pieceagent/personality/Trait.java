package chessagents.agents.pieceagent.personality;

import chessagents.agents.pieceagent.planner.goal.MaximiseCapturedPieces;
import chessagents.agents.pieceagent.planner.goal.RandomValue;
import chessagents.agents.pieceagent.planner.goal.Value;
import chessagents.agents.pieceagent.planner.goal.ProtectPiecesValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
enum Trait {
    AGGRESSIVE(new Value[]{new MaximiseCapturedPieces()}),
    DEFENSIVE(new Value[]{new ProtectPiecesValue()}),
    //    EGOTISTIC(new Value[]{new MaximiseMySafety(), new MaxmiseMyCaptures()}),
//    BENEVOLENT(new Value[]{Value.MAXIMISE_OTHERS_ACTIONS}),
    CHAOTIC(new Value[]{new RandomValue()});

    private final Set<Value> appealingValues;

    Trait(Value[] values) {
        appealingValues = new HashSet<>(Arrays.asList(values));
    }
}

