package chessagents.agents.pieceagent.argumentation.discussionactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.*;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

import java.util.NoSuchElementException;

@AllArgsConstructor
public class VoiceOpinionProposeAlternative extends ConversationAction {
    private final PieceAgent pieceAgent;
    private final TurnDiscussion turnDiscussion;
    private final MoveResponse response;

    @Override
    public String grammarTag() {
        return "<Voice" + response.getOpinion().name() + "ProposeAlternative>";
    }

    @Override
    public ConversationMessage perform() {
        // find trait that has value used, use its grammar to build statement
        var personality = pieceAgent.getPieceContext().getPersonality();
        var randomTraitChooser = new RandomUtil<Trait>();
        var reasoning = response.getReasoning();
        var traitResponsible = randomTraitChooser.chooseRandom(personality.getTraitsThatHaveValue(reasoning.getValue()));

        MoveResponse alternativeMoveResponse;
        int randomInt = RandomUtil.nextInt(2);
        switch (randomInt) {
            default:
            case 0:
                try {
                    alternativeMoveResponse = new ProposeMove(pieceAgent, turnDiscussion).perform().getMoveResponse()
                            .orElseGet(() -> new RevisitMove(pieceAgent, turnDiscussion).perform().getMoveResponse().get());
                } catch (NoSuchElementException e) {
                    return new Quip(pieceAgent, turnDiscussion).perform();
                }
                break;
            case 1:
                try {
                    alternativeMoveResponse = new ProposeMoveWithJustification(pieceAgent, turnDiscussion).perform().getMoveResponse()
                            .orElseGet(() -> new RevisitMove(pieceAgent, turnDiscussion).perform().getMoveResponse().get());
                } catch (NoSuchElementException e) {
                    return new Quip(pieceAgent, turnDiscussion).perform();
                }
                break;
        }

        response.setAlternativeResponse(alternativeMoveResponse);

        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setMoveResponse(response);

        var gameState = pieceAgent.getPieceContext().getGameState();
        grammarVariableProvider.setMovingPiece(getMovingPiece(response, pieceAgent, gameState));
        grammarVariableProvider.setAlternativeMovingPiece(getMovingPiece(response.getAlternativeResponse().get(), pieceAgent, gameState));

        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), response, pieceAgent.getAID());
    }
}
