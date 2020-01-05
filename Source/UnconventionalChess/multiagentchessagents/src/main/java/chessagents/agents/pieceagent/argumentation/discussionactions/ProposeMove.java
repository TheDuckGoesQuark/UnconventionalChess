package chessagents.agents.pieceagent.argumentation.discussionactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.*;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
public class ProposeMove extends ConversationAction {
    private final PieceAgent pieceAgent;
    private final TurnDiscussion turnDiscussion;

    @Override
    public ConversationMessage perform() {
        return getMessage();
    }

    protected Set<PieceMove> getProposableMoves() {
        var possibleMoves = pieceAgent.getPieceContext().getGameState().getAllLegalMoves();

        // remove previously discussed moves
        turnDiscussion.getPreviouslyDiscussedMoves().forEach(possibleMoves::remove);

        return possibleMoves;
    }

    private ConversationMessage getMessage() {
        var possibleMoves = getProposableMoves();
        var pieceContext = pieceAgent.getPieceContext();
        var personality = pieceContext.getPersonality();
        var gameState = pieceContext.getGameState();
        var responses = personality.getResponseToMoves(pieceContext.getMyPiece(), possibleMoves, gameState);

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
        } else if (responsesByOpinion.get(Opinion.DISLIKE).size() > 0) {
            chosenResponse = randomResponseChooser.chooseRandom(responsesByOpinion.get(Opinion.DISLIKE));
        } else {
            // worst case there are no moves we can propose, fallback to quip
            return new Quip(pieceAgent, turnDiscussion).perform();
        }

        // find trait that has value used, use its grammar to build statement
        var randomTraitChooser = new RandomUtil<Trait>();
        var reasoning = chosenResponse.getReasoning();
        var traitResponsible = randomTraitChooser.chooseRandom(personality.getTraitsThatHaveValue(reasoning.getValue()));
        var movingPiece = getMovingPiece(chosenResponse, pieceAgent, gameState);
        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setMoveResponse(chosenResponse);
        grammarVariableProvider.setMovingPiece(movingPiece);

        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), chosenResponse, pieceAgent.getAID());
    }
}
