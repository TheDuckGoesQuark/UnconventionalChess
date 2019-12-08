package chessagents.agents.pieceagent.goals;

import chessagents.agents.pieceagent.ActionResponse;
import chessagents.chess.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.util.RandomUtil;
import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;

import java.util.Random;

import static chessagents.agents.pieceagent.nlg.NLGUtil.NLG_FACTORY;

public class RandomValue extends Value {

    public RandomValue() {
        super("No value, random!");
    }

    @Override
    public ActionResponse getActionResponse(ChessPiece chessPiece, GameState gameState, PieceAction action) {
        var sentence = NLG_FACTORY.createClause();
        var approves = RandomUtil.randBool();

        var me = NLG_FACTORY.createNounPhrase("me");
        var want = NLG_FACTORY.createVerbPhrase("want to"); // a phrasal verb apparently!
        sentence.setSubject(me);
        sentence.setVerb(want);

        if (!approves) sentence.setFeature(Feature.NEGATED, true);

        return new ActionResponse(action, approves, sentence);
    }
}
