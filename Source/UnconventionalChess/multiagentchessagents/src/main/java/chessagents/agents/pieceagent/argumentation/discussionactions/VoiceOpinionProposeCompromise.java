package chessagents.agents.pieceagent.argumentation.discussionactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.*;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;
import jade.util.Logger;
import lombok.AllArgsConstructor;

import java.util.NoSuchElementException;

@AllArgsConstructor
public class VoiceOpinionProposeCompromise extends ConversationAction {
    private final PieceAgent pieceAgent;
    private final TurnDiscussion turnDiscussion;
    private final MoveResponse response;
    private final ConversationMessage lastMessage;

    @Override
    public String grammarTag() {
        return "<Voice" + response.getOpinion().name() + "ProposeCompromise>";
    }

    @Override
    public ConversationMessage perform() {
        // choose alternative using only moves that satisfy the other agents constraint
        var otherAgentReasoning = lastMessage.getMoveResponse().get().getReasoning();
        var optAltResponse = new ProposeCompromise(pieceAgent, turnDiscussion, otherAgentReasoning.getValue(), response).perform();

        return optAltResponse.getMoveResponse().map(alternativeResponse -> {
            // construct new move response with other agent reasoning for grammar variable provider
            var grammarResponse = MoveResponse.buildResponse(response.getMove().get(), response.getOpinion(), otherAgentReasoning);

            // if we both had the same reasoning, its not really a compromise, so just fallback to voice opinion instead
            if (alternativeResponse.getReasoning().getValue().equals(otherAgentReasoning.getValue())) {
                logger.warning("Reasoning was shared, voicing opinion instead");
                return new VoiceOpinion(pieceAgent, turnDiscussion, response).perform();
            } else {
                // adjust the grammar response so it gives the same wording as the previous message in the justification
                grammarResponse.setAlternativeResponse(alternativeResponse);
                response.setAlternativeResponse(alternativeResponse);

                var grammarVariableProvider = new GrammarVariableProviderImpl();
                grammarVariableProvider.setMoveResponse(grammarResponse);

                var gameState = pieceAgent.getPieceContext().getGameState();
                grammarVariableProvider.setMovingPiece(getMovingPiece(grammarResponse, pieceAgent, gameState));
                grammarVariableProvider.setAlternativeMovingPiece(getMovingPiece(grammarResponse.getAlternativeResponse().get(), pieceAgent, gameState));

                var personality = pieceAgent.getPieceContext().getPersonality();
                var traitResponsible = new RandomUtil<Trait>().chooseRandom(personality.getTraits());
                return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), response, pieceAgent.getAID());
            }
        }).orElse(optAltResponse);
    }
}
