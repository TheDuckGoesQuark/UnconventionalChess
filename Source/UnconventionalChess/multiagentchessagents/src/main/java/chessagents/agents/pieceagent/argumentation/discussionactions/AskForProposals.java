package chessagents.agents.pieceagent.argumentation.discussionactions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationAction;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.argumentation.GrammarVariableProviderImpl;
import chessagents.agents.pieceagent.argumentation.TurnDiscussion;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AskForProposals extends ConversationAction {

    private final PieceAgent pieceAgent;
    private final TurnDiscussion turnDiscussion;

    @Override
    public ConversationMessage perform() {
        return createAskForProposalMessage();
    }

    private ConversationMessage createAskForProposalMessage() {
        var personality = pieceAgent.getPieceContext().getPersonality();
        var randomTraitChooser = new RandomUtil<Trait>();
        var traitResponsible = randomTraitChooser.chooseRandom(personality.getTraits());
        var grammarVariableProvider = new GrammarVariableProviderImpl(null, null, null);
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), null, pieceAgent.getAID());
    }
}
