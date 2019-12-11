package chessagents.agents.pieceagent.goals;

import chessagents.agents.pieceagent.ActionResponse;
import chessagents.chess.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;
import simplenlg.features.Feature;
import simplenlg.phrasespec.VPPhraseSpec;

import static chessagents.agents.pieceagent.nlg.NLGUtil.NLG_FACTORY;

public class MinimiseFriendlyThreatenedPieces extends Value {
    public MinimiseFriendlyThreatenedPieces() {
        super("Protect pieces");
    }

    @Override
    public ActionResponse getActionResponse(ChessPiece chessPiece, GameState gameState, PieceAction action) {
        var sentence = NLG_FACTORY.createClause();

        var myColour = chessPiece.getColour();
        var threatenedPiecesBefore = gameState.getThreatenedPieces().stream().filter(p -> p.isColour(myColour)).count();
        var threatenedPiecesAfter = gameState.getOutcomeOfAction(action).getThreatenedPieces().stream().filter(p -> p.isColour(myColour)).count();
        var approves = threatenedPiecesBefore > threatenedPiecesAfter || (threatenedPiecesBefore == 0 && threatenedPiecesAfter == 0);

        var move = NLG_FACTORY.createNounPhrase("move");
        move.setDeterminer("that");
        sentence.setSubject(move);

        final VPPhraseSpec verb;
        if (threatenedPiecesAfter == threatenedPiecesBefore) {
            verb = NLG_FACTORY.createVerbPhrase("affect");
  z          sentence.setFeature(Feature.NEGATED, true);
        } else if (threatenedPiecesAfter < threatenedPiecesBefore) {
            verb = NLG_FACTORY.createVerbPhrase("decrease");
        } else {
            verb = NLG_FACTORY.createVerbPhrase("increase");
        }
        sentence.setVerb(verb);

        var numberOfThreatenedPieces = NLG_FACTORY.createNounPhrase("the number of our threatened pieces");
        sentence.setObject(numberOfThreatenedPieces);

        return new ActionResponse(action, approves, sentence);
    }
}
