package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.*;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.agents.pieceagent.personality.values.Value;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

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
        var otherAgentReasoning = lastMessage.getMoveResponse().get().getReasoning();

        // find trait that has value used, use its grammar to build statement
        var personality = pieceAgent.getPieceContext().getPersonality();
        var randomTraitChooser = new RandomUtil<Trait>();
        var reasoning = response.getReasoning();
        var traitResponsible = randomTraitChooser.chooseRandom(personality.getTraitsThatHaveValue(reasoning.getValue()));

        // choose alternative using only moves that satisfy the other agents constraint
        // TODO consider all previous constraints??
        var optAltResponse = new ProposeCompromise(pieceAgent, turnDiscussion, otherAgentReasoning.getValue()).perform();

        // if we both had the same reasoning, its not really a compromise, so just voice opinion instead
        if (reasoning.getValue().equals(otherAgentReasoning.getValue())) {
            return new VoiceOpinion(pieceAgent, turnDiscussion, response).perform();
        }

        return optAltResponse.getMoveResponse().map(alternativeResponse -> {
            // construct new move response with other agent reasoning for grammar variable provider
            var grammarResponse = MoveResponse.buildResponse(response.getMove().get(), response.getOpinion(), otherAgentReasoning);
            grammarResponse.setAlternativeResponse(alternativeResponse);
            response.setAlternativeResponse(alternativeResponse);
            var grammarVariableProvider = new GrammarVariableProviderImpl(grammarResponse, getMovingPiece(response, pieceAgent), getMovingPiece(response.getAlternativeResponse().get(), pieceAgent));

            return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), response, pieceAgent.getAID());
        }).orElse(optAltResponse);
    }
}
