package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.*;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class ProposeMove extends ConversationAction {
    private final PieceAgent pieceAgent;
    private final TurnDiscussion turnDiscussion;

    @Override
    public ConversationMessage perform() {
        return lastDiscussedMove();
    }

    private ConversationMessage lastDiscussedMove() {
        var possibleMoves = pieceAgent.getPieceContext().getGameState().getAllLegalMoves();

        // remove previously discussed moves
        turnDiscussion.getPreviouslyDiscussedMoves().forEach(possibleMoves::remove);

        var pieceContext = pieceAgent.getPieceContext();
        var personality = pieceContext.getPersonality();
        var responses = personality.getResponseToMoves(pieceContext.getMyPiece(), possibleMoves, pieceContext.getGameState());

        var responsesByOpinion = new HashMap<Opinion, Set<MoveResponse>>();
        responsesByOpinion.put(Opinion.LIKE, new HashSet<>());
        responsesByOpinion.put(Opinion.NEUTRAL, new HashSet<>());
        responsesByOpinion.put(Opinion.DISLIKE, new HashSet<>());

        for (MoveResponse response : responses) {
            responsesByOpinion.get(response.getOpinion()).add(response);
        }

        final MoveResponse chosenResponse;
        var randomResponseChooser = new RandomUtil<MoveResponse>();
        if (responsesByOpinion.get(Opinion.LIKE).size() > 0) {
            chosenResponse = randomResponseChooser.chooseRandom(responsesByOpinion.get(Opinion.LIKE));
        } else if (responsesByOpinion.get(Opinion.NEUTRAL).size() > 0) {
            chosenResponse = randomResponseChooser.chooseRandom(responsesByOpinion.get(Opinion.NEUTRAL));
        } else {
            // worst case we have to propose a move we dont like
            chosenResponse = randomResponseChooser.chooseRandom(responsesByOpinion.get(Opinion.DISLIKE));
        }

        // find trait that has value used, use its grammar to build statement
        var randomTraitChooser = new RandomUtil<Trait>();
        var reasoning = chosenResponse.getReasoning();
        var traitResponsible = randomTraitChooser.chooseRandom(personality.getTraitsThatHaveValue(reasoning.getValue()));
        var movingPiece = getMovingPiece(chosenResponse, pieceAgent);
        var grammarVariableProvider = new GrammarVariableProviderImpl(chosenResponse, movingPiece, null);

        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), chosenResponse, pieceAgent.getAID());
    }
}
