package chessagents.agents.pieceagent.argumentation.discussionactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.*;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VoiceOpinion extends ConversationAction {
    private final PieceAgent pieceAgent;
    private final TurnDiscussion turnDiscussion;
    private final MoveResponse response;

    @Override
    public String grammarTag() {
        return "<Voice" + response.getOpinion().name() + ">";
    }

    @Override
    public ConversationMessage perform() {
        // find trait that has value used, use its grammar to build statement
        var personality = pieceAgent.getPieceContext().getPersonality();
        var randomTraitChooser = new RandomUtil<Trait>();
        var reasoning = response.getReasoning();
        var traitResponsible = randomTraitChooser.chooseRandom(personality.getTraitsThatHaveValue(reasoning.getValue()));
        var movingPiece = getMovingPiece(response, pieceAgent, pieceAgent.getPieceContext().getGameState());
        var grammarVariableProvider = new GrammarVariableProviderImpl();
        grammarVariableProvider.setMoveResponse(response);
        grammarVariableProvider.setMovingPiece(movingPiece);
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), response, pieceAgent.getAID());
    }
}
