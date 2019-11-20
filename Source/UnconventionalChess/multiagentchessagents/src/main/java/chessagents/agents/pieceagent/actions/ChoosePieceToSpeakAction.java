package chessagents.agents.pieceagent.actions;

import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.concepts.ChessPiece;
import lombok.Getter;

@Getter
public class ChoosePieceToSpeakAction extends PieceAction {

    private final ChessPiece chessPieceToSpeak;

    public ChoosePieceToSpeakAction(ChessPiece actor, ChessPiece chessPieceToSpeak) {
        super(PieceTransition.SPEAKER_CHOSEN, "Choose piece to speak", actor);
        this.chessPieceToSpeak = chessPieceToSpeak;
    }
}
