package chessagents.agents.pieceagent.argumentation.discussionactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.*;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

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
                alternativeMoveResponse = new ProposeMove(pieceAgent, turnDiscussion).perform().getMoveResponse().get();
                break;
            case 1:
                alternativeMoveResponse = new ProposeMoveWithJustification(pieceAgent, turnDiscussion).perform().getMoveResponse().get();
                break;
        }

        response.setAlternativeResponse(alternativeMoveResponse);

        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setMoveResponse(response);
        grammarVariableProvider.setMovingPiece(getMovingPiece(response, pieceAgent));
        grammarVariableProvider.setAlternativeMovingPiece(getMovingPiece(response.getAlternativeResponse().get(), pieceAgent));

        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), response, pieceAgent.getAID());
    }
}
