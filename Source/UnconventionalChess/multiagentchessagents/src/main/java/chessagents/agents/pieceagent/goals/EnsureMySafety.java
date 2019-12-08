package chessagents.agents.pieceagent.goals;

import chessagents.agents.pieceagent.ActionResponse;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.util.RandomUtil;
import simplenlg.features.Feature;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;

import java.util.Arrays;

import static chessagents.agents.pieceagent.nlg.NLGUtil.NLG_FACTORY;

public class EnsureMySafety extends Value {

    public EnsureMySafety() {
        super("Ensure my safety");
    }

    public boolean actionMaintainsValue(ChessPiece pieceWithValue, GameState gameState, PieceAction pieceAction) {
        // choose action that produces state where I am not captured and not in the set of captured pieces
        var afterActionState = gameState.getOutcomeOfAction(pieceAction);

        // TODO contains check will fail if I moved/was captured as a clone is made with a different position
        // and current piece hashcode includes the position in the equals check
        // might need to apply some sort of ID to all the pieces, agent or not
        return !afterActionState.getThreatenedPieces().contains(pieceWithValue)
                && afterActionState.getCapturedForColour(pieceWithValue.getColour()).contains(pieceWithValue);
    }

    @Override
    public ActionResponse getActionResponse(ChessPiece chessPiece, GameState gameState, PieceAction action) {
        var approves = actionMaintainsValue(chessPiece, gameState, action);
        var reasoning = generateReasoning(approves);
        return new ActionResponse(action, approves, reasoning);
    }

    private SPhraseSpec generateReasoning(boolean approves) {
        var sentence = NLG_FACTORY.createClause();

        var theMove = NLG_FACTORY.createNounPhrase("move");
        theMove.setDeterminer("that");
        sentence.setObject(theMove);

        var me = NLG_FACTORY.createNounPhrase("me");
        sentence.setSubject(me);

        var verb = new RandomUtil<String>().chooseRandom(Arrays.asList("rescue", "protect", "keep safe"));
        sentence.setVerb(verb);

        if (!approves) sentence.setFeature(Feature.NEGATED, true);

        return sentence;
    }
}
