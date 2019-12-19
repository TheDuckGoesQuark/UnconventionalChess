package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.argumentation.Opinion;
import chessagents.agents.pieceagent.argumentation.TurnDiscussion;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PerformMove implements ConversationAction {

    private final PieceAgent pieceAgent;
    private final TurnDiscussion turnDiscussion;

    @Override
    public ConversationMessage perform() {
        var move = turnDiscussion.getLastMoveDiscussed();

        var myPos = pieceAgent.getPieceContext().getMyPiece().getPosition();
        var possibleMoves = pieceAgent.getPieceContext().getGameState().getAllLegalMoves()
                .stream()
                .filter(m -> m.getSource().equals(myPos))
                .collect(Collectors.toSet());

        if (move == null || !possibleMoves.contains(move)) {
            move = new RandomUtil<PieceMove>().chooseRandom(possibleMoves);
        }

        var pieceContext = pieceAgent.getPieceContext();
        var personality = pieceContext.getPersonality();
        var responses = personality.getResponseToMoves(pieceContext.getMyPiece(), Collections.singleton(move), pieceContext.getGameState());

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
            chosenResponse = randomResponseChooser.chooseRandom(responsesByOpinion.get(Opinion.DISLIKE));
        }

        // find trait that has value used, use its grammar to build statement
        var randomTraitChooser = new RandomUtil<Trait>();
        var traitResponsible = randomTraitChooser.chooseRandom(personality.getTraitsThatHaveValue(chosenResponse.getOpinionGeneratingValue()));

        chosenResponse.setPerformed(true);

        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag()), chosenResponse, pieceAgent.getAID());
    }
}
