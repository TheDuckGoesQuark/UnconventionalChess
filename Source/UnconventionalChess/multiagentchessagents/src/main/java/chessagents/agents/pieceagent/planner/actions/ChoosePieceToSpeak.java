package chessagents.agents.pieceagent.planner.actions;

import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.planner.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;
import lombok.Getter;

@Getter
public class ChoosePieceToSpeak extends PieceAction {

    private final ChessPiece chessPieceToSpeak;

    public ChoosePieceToSpeak(ChessPiece actor, ChessPiece chessPieceToSpeak) {
        super(PieceTransition.SPEAKER_CHOSEN, "Choose piece to speak", actor);
        this.chessPieceToSpeak = chessPieceToSpeak;
    }
}
