package chessagents.agents.pieceagent.goals;

import chessagents.agents.pieceagent.ActionResponse;
import chessagents.chess.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;
import simplenlg.features.Feature;
import simplenlg.phrasespec.VPPhraseSpec;

import static chessagents.agents.pieceagent.nlg.NLGUtil.NLG_FACTORY;

public class MaximiseEnemyCapturedPieces extends Value {

    public MaximiseEnemyCapturedPieces() {
        super("Maximise captured pieces");
    }

    @Override
    public ActionResponse getActionResponse(ChessPiece chessPiece, GameState gameState, PieceAction action) {
        var enemiesCapturedBefore = gameState.getCapturedForColour(chessPiece.getColour()).size();
        var enemiesCapturedAfter = gameState.getOutcomeOfAction(action).getCapturedForColour(chessPiece.getColour()).size();
        var approved = enemiesCapturedBefore < enemiesCapturedAfter;

        var sentence = NLG_FACTORY.createClause();
        var move = NLG_FACTORY.createNounPhrase("move");
        move.setDeterminer("that");
        sentence.setSubject(move);
        var captures = NLG_FACTORY.createVerbPhrase("capture");

        if (!approved) {
            sentence.setFeature(Feature.NEGATED, true);
        }
        sentence.setVerb(captures);

        var enemyPiece = NLG_FACTORY.createNounPhrase("piece");
        enemyPiece.setDeterminer("an enemy");
        sentence.setObject(enemyPiece);

        return new ActionResponse(action, approved, sentence);
    }
}
