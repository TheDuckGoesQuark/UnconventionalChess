package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.MoveResponse;
import chessagents.agents.pieceagent.argumentation.Opinion;
import chessagents.agents.pieceagent.argumentation.TurnDiscussion;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VoiceOpinionWithJustification implements ConversationAction {
    private final PieceAgent pieceAgent;
    private final TurnDiscussion turnDiscussion;
    private final MoveResponse response;

    @Override
    public ConversationMessage perform() {
        // find trait that has value used, use its grammar to build statement
        var personality = pieceAgent.getPieceContext().getPersonality();
        var randomTraitChooser = new RandomUtil<Trait>();
        var traitResponsible = randomTraitChooser.chooseRandom(personality.getTraitsThatHaveValue(response.getOpinionGeneratingValue()));
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag()), response, pieceAgent.getAID());
    }
}
